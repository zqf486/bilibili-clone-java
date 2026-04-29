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
 * 视频处理任务表
 * </p>
 *
 * @author zqf486
 * @since 2026-04-29
 */
@Getter
@Setter
@ToString
@TableName("tb_video_task")
public class TbVideoTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID (UUID)
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联视频ID
     */
    @TableField("video_id")
    private Long videoId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 进度 0-100
     */
    @TableField("progress")
    private Integer progress;

    /**
     * 当前步骤描述
     */
    @TableField("step")
    private String step;

    /**
     * 候选封面JSON [{url, index}]
     */
    @TableField("covers")
    private String covers;

    /**
     * 用户选择的封面索引
     */
    @TableField("cover_selected")
    private Integer coverSelected;

    /**
     * 0处理中 1待选封面 2完成 3失败
     */
    @TableField("status")
    private Byte status;

    /**
     * 错误信息
     */
    @TableField("error_msg")
    private String errorMsg;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
