package com.bilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.context.UserContext;
import com.bilibili.dto.UploadInitDTO;
import com.bilibili.entity.TbUploadTask;
import com.bilibili.mapper.UploadTaskMapper;
import com.bilibili.service.IVideoTaskService;
import com.bilibili.util.S3Util;
import com.bilibili.vo.UploadTaskVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.Part;

import java.io.InputStream;
import java.util.List;

@Service
public class VideoTaskService implements IVideoTaskService {

    @Resource
    private UploadTaskMapper uploadTaskMapper;

    @Resource
    private S3Util s3Util;

    @Override
    @Transactional
    public UploadTaskVO init(UploadInitDTO uploadInitDTO) {
        Long userId = UserContext.get().getId();

        LambdaQueryWrapper<TbUploadTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbUploadTask::getMd5, uploadInitDTO.getMd5())
                .eq(TbUploadTask::getUserId, userId)
                .eq(TbUploadTask::getStatus, 0);
        TbUploadTask uploadTask = uploadTaskMapper.selectOne(wrapper);

        if (uploadTask != null) {
            UploadTaskVO vo = BeanUtil.copyProperties(uploadTask, UploadTaskVO.class);
            List<Part> parts = s3Util.listParts(uploadTask.getObjectName(), uploadTask.getUploadId());
            vo.setUploadedChunks(parts.stream().map(Part::partNumber).toList());
            return vo;
        }

        long fileSize = uploadInitDTO.getFileSize();
        int chunkSize = calcChunkSize(fileSize);
        int totalChunks = calcTotalChunks(fileSize, chunkSize);
        String objectName = "video/" + userId + "/" + uploadInitDTO.getMd5() + "/original";
        String uploadId = s3Util.createMultipartUpload(objectName);

        uploadTask = TbUploadTask.builder()
                .userId(userId)
                .md5(uploadInitDTO.getMd5())
                .fileSize(fileSize)
                .chunkSize(chunkSize)
                .totalChunks(totalChunks)
                .uploadId(uploadId)
                .objectName(objectName)
                .status((byte) 0)
                .build();

        uploadTaskMapper.insert(uploadTask);

        UploadTaskVO vo = BeanUtil.copyProperties(uploadTask, UploadTaskVO.class);
        vo.setUploadedChunks(List.of());
        return vo;
    }

    @Override
    public void uploadChunk(Long taskId, Integer partNumber, MultipartFile file) {
        TbUploadTask task = uploadTaskMapper.selectById(taskId);

        if (task == null) {
            throw new RuntimeException("上传任务不存在");
        }

        if (!task.getUserId().equals(UserContext.get().getId())) {
            throw new RuntimeException("无权操作此任务");
        }

        try (InputStream is = file.getInputStream()) {
            s3Util.uploadPart(task.getObjectName(), task.getUploadId(), partNumber, is, file.getSize());
        } catch (Exception e) {
            throw new RuntimeException("分片上传失败", e);
        }
    }

    private static final int MB = 1024 * 1024;

    private int calcChunkSize(long fileSize) {
        if (fileSize <= 100 * MB) {
            return MB;
        } else if (fileSize <= 1024 * MB) {
            return 5 * MB;
        } else {
            return 10 * MB;
        }
    }

    private int calcTotalChunks(long fileSize, int chunkSize) {
        return (int) Math.ceil((double) fileSize / chunkSize);
    }
}
