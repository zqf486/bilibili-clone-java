package com.bilibili.init;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.entity.TbUploadTask;
import com.bilibili.mapper.UploadTaskMapper;
import com.bilibili.util.S3Util;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class UploadTaskCleaner {

    @Resource
    private UploadTaskMapper uploadTaskMapper;

    @Resource
    private S3Util s3Util;

    @Scheduled(fixedRate = 3600000)
    public void cleanStaleTasks() {
        List<TbUploadTask> staleTasks = uploadTaskMapper.selectList(
                new LambdaQueryWrapper<TbUploadTask>()
                        .eq(TbUploadTask::getStatus, 0)
                        .lt(TbUploadTask::getCreateTime, LocalDateTime.now().minusHours(24))
        );

        for (TbUploadTask task : staleTasks) {
            try {
                s3Util.abortMultipartUpload(task.getObjectName(), task.getUploadId());
                task.setStatus((byte) 3);
                uploadTaskMapper.updateById(task);
                log.info("清理过期上传任务: taskId={}, objectName={}", task.getId(), task.getObjectName());
            } catch (Exception e) {
                log.error("清理任务失败: taskId={}", task.getId(), e);
            }
        }
    }
}
