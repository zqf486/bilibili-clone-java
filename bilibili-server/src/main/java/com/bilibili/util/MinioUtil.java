package com.bilibili.util;

import io.minio.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
public class MinioUtil {

    @Resource
    private MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucket}")
    private String bucket;

    /**
     * 初始化 bucket，不存在则自动创建
     */
    @PostConstruct
    public void init() {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build());
                log.info("MinIO bucket [{}] created", bucket);
            }
        } catch (Exception e) {
            log.error("MinIO bucket init failed", e);
        }
    }

    /**
     * 上传文件（MultipartFile）
     *
     * @param file       文件
     * @param objectName 对象名（如 video/uuid.mp4）
     * @return 文件访问 URL
     */
    public String upload(MultipartFile file, String objectName) {
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(is, file.getSize(), -1L)
                    .contentType(file.getContentType())
                    .build());
            return getUrl(objectName);
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    /**
     * 上传文件流
     *
     * @param stream      文件流
     * @param objectName  对象名
     * @param contentType 内容类型（如 video/mp4）
     * @param size        文件大小
     * @return 文件访问 URL
     */
    public String upload(InputStream stream, String objectName, String contentType, long size) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(stream, size, -1L)
                    .contentType(contentType)
                    .build());
            return getUrl(objectName);
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    /**
     * 获取文件访问 URL（需 bucket 为公开读）
     *
     * @param objectName 对象名
     * @return 永久有效的文件 URL
     */
    public String getUrl(String objectName) {
        return endpoint + "/" + bucket + "/" + objectName;
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名
     */
    public void delete(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("File delete failed: {}", objectName, e);
        }
    }

    /**
     * 生成唯一文件名
     * <p>
     * 规则: {prefix}/{uuid}.{suffix}
     *
     * @param prefix 目录前缀（avatar / video / cover）
     * @param suffix 文件后缀（jpg / mp4）
     * @return 唯一文件名
     */
    public String generateFileName(String prefix, String suffix) {
        return prefix + "/" + UUID.randomUUID() + "." + suffix;
    }
}
