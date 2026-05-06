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
 * 视频分集表
 * </p>
 *
 * @author zqf486
 * @since 2026-05-06
 */
@Getter
@Setter
@ToString
@TableName("tb_video_episode")
public class TbVideoEpisode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分集ID（主键）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 所属投稿ID
     */
    @TableField("video_id")
    private Long videoId;

    /**
     * 集数 (从1开始)
     */
    @TableField("episode_index")
    private Integer episodeIndex;

    /**
     * 分集标题
     */
    @TableField("title")
    private String title;

    /**
     * 时长（秒）
     */
    @TableField("duration")
    private Integer duration;

    /**
     * 播放量
     */
    @TableField("views")
    private Integer views;

    /**
     * 转码状态：0待处理 1处理中 2完成 3失败
     */
    @TableField("transcode_status")
    private Byte transcodeStatus;

    /**
     * 转码错误信息
     */
    @TableField("error_msg")
    private String errorMsg;

    /**
     * 逻辑删除
     */
    @TableField("is_delete")
    private Byte isDelete;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
