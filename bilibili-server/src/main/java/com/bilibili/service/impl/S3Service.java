package com.bilibili.service.impl;

import com.bilibili.service.IS3Service;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class S3Service implements IS3Service {

    @Resource
    private S3Client s3Client;

    @Resource
    private S3Presigner s3Presigner;

    /**
     * 生成预签名上传链接
     *
     * @param bucket
     * @param objectKey
     * @return
     */
    @Override
    public String generateUploadUrl(String bucket, String objectKey) {
        try {
            PutObjectPresignRequest request = PutObjectPresignRequest.builder()
                    .putObjectRequest(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(objectKey)
                            .build())
                    .signatureDuration(Duration.ofHours(1))
                    .build();
            return s3Presigner.presignPutObject(request).url().toString();
        } catch (Exception e) {
            log.error("生成上传预签名URL失败", e);
            throw new RuntimeException("生成上传预签名URL失败", e);
        }
    }

    /**
     * 生成预签名下载链接
     *
     * @param bucket
     * @param objectKey
     * @return
     */
    @Override
    public String generateDownloadUrl(String bucket, String objectKey) {
        try {
            GetObjectPresignRequest request = GetObjectPresignRequest.builder()
                    .getObjectRequest(GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(objectKey)
                            .build())
                    .signatureDuration(Duration.ofDays(7))
                    .build();
            return s3Presigner.presignGetObject(request).url().toString();
        } catch (Exception e) {
            log.error("生成下载预签名URL失败", e);
            throw new RuntimeException("生成下载预签名URL失败", e);
        }
    }

    /**
     * 删除对象
     *
     * @param bucket
     * @param objectKey
     */
    @Override
    public void deleteObject(String bucket, String objectKey) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build());
        } catch (Exception e) {
            log.error("删除对象失败: bucket={}, key={}", bucket, objectKey, e);
            throw new RuntimeException("删除对象失败", e);
        }
    }

    /**
     * 判断对象是否存在
     *
     * @param bucket
     * @param objectKey
     * @return
     */
    @Override
    public boolean objectExists(String bucket, String objectKey) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build());
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            log.error(
                    "检查对象是否存在失败: bucket={}, key={}",
                    bucket,
                    objectKey,
                    e
            );
            throw new RuntimeException("检查对象是否存在失败", e);
        }
    }

    /**
     * 列举对象
     *
     * @param bucket
     * @return
     */
    @Override
    public List<String> listObjects(String bucket) {
        List<String> keys = new ArrayList<>();
        try {
            ListObjectsV2Response response = s3Client.listObjectsV2(
                    ListObjectsV2Request.builder().bucket(bucket).build());
            response.contents().forEach(obj -> keys.add(obj.key()));
        } catch (Exception e) {
            log.error("列举对象失败: bucket={}", bucket, e);
            throw new RuntimeException("列举对象失败", e);
        }
        return keys;
    }

    /**
     * 创建分段上传
     *
     * @param bucket
     * @param objectKey
     * @return uploadId
     */
    @Override
    public String createMultipartUpload(String bucket, String objectKey) {
        try {
            CreateMultipartUploadResponse response = s3Client.createMultipartUpload(
                    CreateMultipartUploadRequest.builder()
                            .bucket(bucket)
                            .key(objectKey)
                            .build()
            );

            return response.uploadId();

        } catch (Exception e) {
            log.error("生成预签名分片上传失败", e);
            throw new RuntimeException("生成预签名分片上传失败", e);
        }
    }

    /**
     * 生成分片上传预签名链接
     *
     * @param bucket
     * @param objectKey
     * @param uploadId   MinIO multipart upload ID
     * @param partNumber 分片数量
     * @return 分片上传预签名链接
     */
    @Override
    public String generateUploadPartUrl(
            String bucket,
            String objectKey,
            String uploadId,
            Integer partNumber
    ) {
        try {

            UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .uploadId(uploadId)
                    .partNumber(partNumber)
                    .build();

            UploadPartPresignRequest partPresignRequest = UploadPartPresignRequest.builder()
                    .uploadPartRequest(uploadPartRequest)
                    .signatureDuration(Duration.ofHours(1))
                    .build();

            return s3Presigner.presignUploadPart(partPresignRequest).url().toString();

        } catch (Exception e) {
            log.error("生成分片上传预签名URL失败", e);
            throw new RuntimeException("生成分片上传预签名URL失败", e);
        }
    }
}
