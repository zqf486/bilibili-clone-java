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
 * ???
 * </p>
 *
 * @author zqf486
 * @since 2026-04-28
 */
@Getter
@Setter
@ToString
@TableName("tb_video")
public class TbVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ??ID????
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * ????
     */
    @TableField("title")
    private String title;

    /**
     * ????
     */
    @TableField("description")
    private String description;

    /**
     * ??????
     */
    @TableField("url")
    private String url;

    /**
     * ???
     */
    @TableField("cover")
    private String cover;

    /**
     * ??ID
     */
    @TableField("category_id")
    private Integer categoryId;

    /**
     * ???ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * ???????
     */
    @TableField("duration")
    private Integer duration;

    /**
     * ???0??? 1??? 2????? 3??
     */
    @TableField("status")
    private Byte status;

    /**
     * ???
     */
    @TableField("views")
    private Integer views;

    /**
     * ???
     */
    @TableField("likes")
    private Integer likes;

    /**
     * ???
     */
    @TableField("favorites")
    private Integer favorites;

    /**
     * ???
     */
    @TableField("coins")
    private Integer coins;

    /**
     * ???
     */
    @TableField("comments")
    private Integer comments;

    /**
     * ????????
     */
    @TableField("tags")
    private String tags;

    /**
     * ?????0??? 1???
     */
    @TableField("is_delete")
    private Byte isDelete;

    /**
     * ????
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * ????
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
