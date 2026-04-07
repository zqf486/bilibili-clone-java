package com.bilibili.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 视频分区表
 * </p>
 *
 * @author zqf486
 * @since 2026-04-07
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_category")
public class TbCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称（如：科技、游戏）
     */
    @TableField("name")
    private String name;

    /**
     * 排序字段（数值越小越靠前）
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 状态：1显示 0隐藏
     */
    @TableField("status")
    private Byte status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
