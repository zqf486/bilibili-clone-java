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
 * 上传任务表
 * </p>
 *
 * @author zqf486
 * @since 2026-05-06
 */
@Getter
@Setter
@ToString
@TableName("tb_upload_task")
public class TbUploadTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID（UUID）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 文件MD5
     */
    @TableField("md5")
    private String md5;

    /**
     * 原始文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件总大小
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 分片大小（字节）
     */
    @TableField("chunk_size")
    private Integer chunkSize;

    /**
     * 总分片数
     */
    @TableField("total_chunks")
    private Integer totalChunks;

    /**
     * 已完成分片数
     */
    @TableField("completed_chunks")
    private Integer completedChunks;

    /**
     * MinIO multipart upload ID
     */
    @TableField("upload_id")
    private String uploadId;

    /**
     * MinIO 对象名
     */
    @TableField("object_name")
    private String objectName;

    /**
     * 状态：0上传中 1上传完成 2已合并 3失败
     */
    @TableField("status")
    private Byte status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
