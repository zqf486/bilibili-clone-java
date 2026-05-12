package com.bilibili.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadInitDTO {

    /**
     * 原始文件名
     */
    @NotBlank(message = "文件名不能为空")
    private String name;

    /**
     * 文件MIME类型
     */
    @NotBlank(message = "文件类型不能为空")
    private String type;

    /**
     * 文件 md5
     */
    @NotBlank(message = "md5值不能为空")
    private String md5;

    /**
     * 文件大小
     */
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;
}
