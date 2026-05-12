package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.context.UserContext;
import com.bilibili.dto.UploadInitDTO;
import com.bilibili.entity.TbUploadTask;
import com.bilibili.enumeration.UploadStatus;
import com.bilibili.mapper.UploadTaskMapper;
import com.bilibili.service.IS3Service;
import com.bilibili.service.IUploadService;
import com.bilibili.vo.UploadInitVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UploadServiceImpl implements IUploadService {

    @Resource
    private UploadTaskMapper uploadTaskMapper;

    @Resource
    private IS3Service s3Service;

    private static final String defaultBuket = "bilibili-clone";

    private static final String uploadKeyPrefix = "tmp/";

    /**
     * 上传初始化
     *
     * @return
     */
    @Override
    public UploadInitVO init(UploadInitDTO dto) {

        Long userId = UserContext.get().getId();

        // 1. 查询上传记录
        LambdaQueryWrapper<TbUploadTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbUploadTask::getMd5, dto.getMd5());
        wrapper.eq(TbUploadTask::getUserId, userId);
        TbUploadTask uploadTask = uploadTaskMapper.selectOne(wrapper);

        // 2.任务存在返回记录
        if (uploadTask != null) {

            List<Integer> parts =
                    s3Service.listUploadedParts(
                            defaultBuket,
                            uploadTask.getObjectName(),
                            uploadTask.getUploadId()
                    );

            return UploadInitVO.builder()
                    .taskId(uploadTask.getId())
                    .uploadId(uploadTask.getUploadId())
                    .chunkSize(uploadTask.getChunkSize())
                    .totalChunks(uploadTask.getTotalChunks())
                    .uploadedParts(parts)
                    .build();
        }

        int chunkSize = 1024 * 5;
        int totalChunks = (int) Math.ceil(
                (double) dto.getFileSize() / chunkSize
        );

        // 3.不存在生成记录 创建记录 生成 MinIO multipart upload ID
        uploadTask = TbUploadTask.builder()
                .userId(userId)
                .md5(dto.getMd5())
                .fileName(dto.getName())
                .mimeType(dto.getType())
                .fileSize(dto.getFileSize())
                .chunkSize(chunkSize)
                .totalChunks(totalChunks)
                .status(UploadStatus.INIT.getCode())
                .expireTime(LocalDateTime.now().plusDays(1))
                .build();

        // 主键回填
        uploadTaskMapper.insert(uploadTask);

        String objectName =
                uploadKeyPrefix
                        + userId
                        + "/"
                        + uploadTask.getId();

        String uploadId =
                s3Service.createMultipartUpload(
                        defaultBuket, objectName
                );

        uploadTask.setUploadId(uploadId);

        uploadTask.setObjectName(objectName);

        uploadTask.setStatus(
                UploadStatus.UPLOADING.getCode()
        );

        uploadTaskMapper.updateById(uploadTask);

        return UploadInitVO.builder()
                .taskId(uploadTask.getId())
                .uploadId(uploadId)
                .chunkSize(chunkSize)
                .totalChunks(totalChunks)
                .uploadedParts(List.of())
                .build();
    }


    /**
     * 心跳续期 task ttl
     *
     * @param taskId 上传任务id
     */
    @Override
    public void heartbeat(Long taskId) {

    }
}
