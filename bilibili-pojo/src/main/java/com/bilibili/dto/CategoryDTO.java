package com.bilibili.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTO {

    /**
     * 分类ID（主键）
     */
    private Integer id;

    /**
     * 排序字段（数值越小越靠前）
     */
    private Integer sort;
}
