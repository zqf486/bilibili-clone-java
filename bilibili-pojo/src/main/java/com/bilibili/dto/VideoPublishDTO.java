package com.bilibili.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VideoPublishDTO {

    @NotBlank(message = "视频标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "分类不能为空")
    private Integer categoryId;

    private String tags;

    private Integer duration;
}
