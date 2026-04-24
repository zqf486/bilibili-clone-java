package com.bilibili.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class CategoryCreateDTO {

    /**
     * 分类编码
     */
    @NotBlank(message = "分类编码不能为空")
    private String code;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 父分类ID
     */
    private Integer pCategoryId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 背景图
     */
    private String background;

    /**
     * 排序
     */
    private Integer sort = 0;
}
