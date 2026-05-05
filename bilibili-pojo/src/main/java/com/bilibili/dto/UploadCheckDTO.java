package com.bilibili.dto;

import lombok.Data;

@Data
public class UploadCheckDTO {

    /**
     * 视频 md5
     */
    private String md5;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;
}
