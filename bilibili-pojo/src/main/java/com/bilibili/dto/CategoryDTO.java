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
}
