package com.bilibili.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadInitDTO {

    /**
     * 视频 md5
     */
    @NotBlank(message = "md5值不能为空")
    private String md5;

    /**
     * 文件大小
     */
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;
}
