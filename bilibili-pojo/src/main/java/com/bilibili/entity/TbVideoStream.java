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
 * 视频流清晰度表
 * </p>
 *
 * @author zqf486
 * @since 2026-05-06
 */
@Getter
@Setter
@ToString
@TableName("tb_video_stream")
public class TbVideoStream implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流ID（主键）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 所属分集ID
     */
    @TableField("episode_id")
    private Long episodeId;

    /**
     * 清晰度：1=480P 2=720P 3=1080P 4=4K SDR 5=4K HDR
     */
    @TableField("quality")
    private Byte quality;

    /**
     * 播放地址
     */
    @TableField("url")
    private String url;

    /**
     * 文件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 视频宽度
     */
    @TableField("width")
    private Integer width;

    /**
     * 视频高度
     */
    @TableField("height")
    private Integer height;

    /**
     * 码率（kbps）
     */
    @TableField("bitrate")
    private Integer bitrate;

    /**
     * 编码格式
     */
    @TableField("codec")
    private String codec;

    /**
     * 状态：0转码中 1可播放
     */
    @TableField("status")
    private Byte status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
