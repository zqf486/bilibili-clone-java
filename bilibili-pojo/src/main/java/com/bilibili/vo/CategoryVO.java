package com.bilibili.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 子分类
     */
    private List<CategoryVO> children;

    /**
     * 分类ID
     */
    private Integer id;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 分类名称
     */
    private String name;

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
    private Integer sort;

    /**
     * 1启用 0禁用
     */
    private Byte status;
}
