package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.MinIOConstant;
import com.bilibili.context.UserContext;
import com.bilibili.dto.UploadInitDTO;
import com.bilibili.entity.TbUploadTask;
import com.bilibili.enumeration.UploadStatus;
import com.bilibili.exception.BusinessException;
import com.bilibili.mapper.UploadTaskMapper;
import com.bilibili.service.IS3Service;
import com.bilibili.service.IVideoUploadService;
import com.bilibili.vo.UploadInitVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class VideoUploadServiceImpl implements IVideoUploadService {

    @Resource
    private UploadTaskMapper uploadTaskMapper;

    @Resource
    private IS3Service s3Service;

    @Value("${upload.chunk-size}")
    private int chunkSize;

    @Value("${upload.task-ttl}")
    private int taskTtl;

    @Value("${minio.bucket}")
    private String bucket;

    /**
     * 分片上传初始化
     *
     * @param dto 上传文件信息
     * @return 上传任务信息
     */
    @Override
    @Transactional
    public UploadInitVO init(UploadInitDTO dto) {

        Long userId = UserContext.get().getId();

        // 1. 查询上传记录
        LambdaQueryWrapper<TbUploadTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbUploadTask::getMd5, dto.getMd5());
        wrapper.eq(TbUploadTask::getUserId, userId);
        wrapper.orderByDesc(TbUploadTask::getCreateTime);
        TbUploadTask uploadTask = uploadTaskMapper.selectOne(wrapper);

        // 2. 任务存在
        if (uploadTask != null) {
            /**
             * 判断是否需要续传 (返回当前上传记录)
             * 唯一一种需要续传的条件:
             * status = init or uploading 未完成
             * expireTime > now 未过期
             */
            if (uploadTask.getStatus() == UploadStatus.INIT.getCode()
                    || uploadTask.getStatus() == UploadStatus.UPLOADING.getCode()
            ) {
                if (uploadTask.getExpireTime().isAfter(LocalDateTime.now())) {

                    // 续传
                    List<Integer> parts =
                            s3Service.listUploadedParts(
                                    bucket,
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
            }
        }

        int totalChunks = (int) Math.ceil(
                (double) dto.getFileSize() / chunkSize
        );

        // 3. 创建新记录 生成 MinIO multipart upload ID
        uploadTask = TbUploadTask.builder()
                .userId(userId)
                .md5(dto.getMd5())
                .fileName(dto.getName())
                .mimeType(dto.getType())
                .fileSize(dto.getFileSize())
                .chunkSize(chunkSize)
                .totalChunks(totalChunks)
                .status(UploadStatus.INIT.getCode())
                .expireTime(LocalDateTime.now().plusDays(taskTtl))
                .build();

        // 主键回填
        uploadTaskMapper.insert(uploadTask);

        // 拼接 objectName
        String objectName = MinIOConstant.TMP + MinIOConstant.VIDEO
                + userId + "/" + uploadTask.getId();

        String uploadId = s3Service.createMultipartUpload(bucket, objectName);

        uploadTask.setUploadId(uploadId);
        uploadTask.setObjectName(objectName);
        uploadTask.setStatus(UploadStatus.UPLOADING.getCode());
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
     * 心跳续期 task TTL
     *
     * @param taskId 上传任务id
     */
    @Override
    public void heartbeat(Long taskId) {

        if (taskId == null) {
            throw new BusinessException(MessageConstant.UPLOAD_TASK_ID_EMPTY);
        }

        TbUploadTask uploadTask = uploadTaskMapper.selectById(taskId);

        if (uploadTask == null) {
            throw new BusinessException(MessageConstant.UPLOAD_TASK_NOT_EXISTS);
        }

        uploadTask.setExpireTime(LocalDateTime.now().plusDays(taskTtl));
        uploadTaskMapper.updateById(uploadTask);
    }
}
