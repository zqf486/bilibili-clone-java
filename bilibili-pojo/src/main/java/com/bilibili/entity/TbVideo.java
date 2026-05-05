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
 * 视频主表
 * </p>
 *
 * @author zqf486
 * @since 2026-05-06
 */
@Getter
@Setter
@ToString
@TableName("tb_video")
public class TbVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频ID（主键）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 视频标题
     */
    @TableField("title")
    private String title;

    /**
     * 视频简介
     */
    @TableField("description")
    private String description;

    /**
     * 封面图
     */
    @TableField("cover")
    private String cover;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Integer categoryId;

    /**
     * 发布者ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 标签（逗号分隔）
     */
    @TableField("tags")
    private String tags;

    /**
     * 总集数
     */
    @TableField("episode_count")
    private Integer episodeCount;

    /**
     * 状态：0审核中 1已发布 2审核不通过 3私密
     */
    @TableField("status")
    private Byte status;

    /**
     * 播放量
     */
    @TableField("views")
    private Integer views;

    /**
     * 点赞数
     */
    @TableField("likes")
    private Integer likes;

    /**
     * 收藏数
     */
    @TableField("favorites")
    private Integer favorites;

    /**
     * 硬币数
     */
    @TableField("coins")
    private Integer coins;

    /**
     * 评论数
     */
    @TableField("comments")
    private Integer comments;

    /**
     * 逻辑删除：0未删除 1已删除
     */
    @TableField("is_delete")
    private Byte isDelete;

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
