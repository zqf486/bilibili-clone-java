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
     *
     * @param bucket
     * @return
     */
    List<String> listObjects(
            String bucket
    );

}
