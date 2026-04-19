package com.bilibili.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDTO {

    /**
     * 分类ID（主键）
     */
    private Integer id;

    /**
     * 分类名称（如：科技、游戏）
     */
    @NotBlank
    @Size(max = 10)
    private String name;

    /**
     * 排序字段（数值越小越靠前）
     */
    @NotNull
    private Integer sort;
}
