package com.bilibili.service;

import java.util.List;

public interface IS3Service {

    /**
     * 生成预签名上传链接
     *
     * @param bucket
     * @param objectKey
     * @return
     */
    String generateUploadUrl(
            String bucket,
            String objectKey
    );

    /**
     * 生成预签名下载链接
     *
     * @param bucket
     * @param objectKey
     * @return
     */
    String generateDownloadUrl(
            String bucket,
            String objectKey
    );

    /**
     * 删除对象
     *
     * @param bucket
     * @param objectKey
     */
    void deleteObject(
            String bucket,
            String objectKey
    );

    /**
     * 对象是否存在
     *
     * @param bucket
     * @param objectKey
     * @return
     */
    boolean objectExists(
            String bucket,
            String objectKey
    );

    /**
     * 列举对象
     *
     * @param bucket
     * @return
     */
    List<String> listObjects(
            String bucket
    );

    /**
     * 创建分段上传
     *
     * @param bucket
     * @param objectKey
     * @return MinIO multipart upload ID
     */
    String createMultipartUpload(
            String bucket,
            String objectKey
    );

    /**
     * 生成分片上传预签名链接
     *
     * @param bucket
     * @param objectKey
     * @param uploadId   MinIO multipart upload ID
     * @param partNumber 分片数量
     * @return 分片上传预签名链接
     */
    String generateUploadPartUrl(
            String bucket,
            String objectKey,
            String uploadId,
            Integer partNumber
    );
}
