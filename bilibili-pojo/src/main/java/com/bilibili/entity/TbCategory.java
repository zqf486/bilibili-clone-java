package com.bilibili.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 视频分区表
 * </p>
 *
 * @author zqf486
 * @since 2026-04-25
 */
@Getter
@Setter
@ToString
@TableName("tb_category")
public class TbCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类编码
     */
    @TableField("code")
    private String code;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 父分类ID
     */
    @TableField("p_category_id")
    private Integer pCategoryId;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 背景图
     */
    @TableField("background")
    private String background;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 1启用 0禁用
     */
    @TableField("status")
    private Byte status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
