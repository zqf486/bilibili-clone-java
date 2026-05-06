package com.bilibili.util;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class S3Util {

    @Resource
    private S3Client s3Client;

    @Value("${minio.bucket}")
    private String bucket;

    public String createMultipartUpload(String objectName) {
        try {
            CreateMultipartUploadResponse response = s3Client.createMultipartUpload(
                    CreateMultipartUploadRequest.builder()
                            .bucket(bucket)
                            .key(objectName)
                            .build());
            return response.uploadId();
        } catch (Exception e) {
            throw new RuntimeException("创建分片上传失败", e);
        }
    }

    public UploadPartResponse uploadPart(String objectName, String uploadId, int partNumber, InputStream data, long size) {
        try {
            return s3Client.uploadPart(UploadPartRequest.builder()
                    .bucket(bucket)
                    .key(objectName)
                    .uploadId(uploadId)
                    .partNumber(partNumber)
                    .contentLength(size)
                    .build(), RequestBody.fromInputStream(data, size));
        } catch (Exception e) {
            throw new RuntimeException("分片上传失败", e);
        }
    }

    public List<Part> listParts(String objectName, String uploadId) {
        try {
            ListPartsResponse response = s3Client.listParts(ListPartsRequest.builder()
                    .bucket(bucket)
                    .key(objectName)
                    .uploadId(uploadId)
                    .build());
            return response.parts();
        } catch (Exception e) {
            throw new RuntimeException("查询分片列表失败", e);
        }
    }

    public void completeMultipartUpload(String objectName, String uploadId, List<CompletedPart> parts) {
        try {
            s3Client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                    .bucket(bucket)
                    .key(objectName)
                    .uploadId(uploadId)
                    .multipartUpload(CompletedMultipartUpload.builder()
                            .parts(parts)
                            .build())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("合并分片失败", e);
        }
    }

    public void abortMultipartUpload(String objectName, String uploadId) {
        try {
            s3Client.abortMultipartUpload(AbortMultipartUploadRequest.builder()
                    .bucket(bucket)
                    .key(objectName)
                    .uploadId(uploadId)
                    .build());
        } catch (Exception e) {
            log.error("取消分片上传失败: {}", objectName, e);
        }
    }
}
