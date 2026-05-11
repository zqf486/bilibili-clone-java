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
 * 上传任务表
 * </p>
 *
 * @author zqf486
 * @since 2026-05-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_upload_task")
public class TbUploadTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID（主键）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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
     * 文件MIME类型
     */
    @TableField("mime_type")
    private String mimeType;

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
     * 状态：0初始化 1上传中 2已完成 3失败 4已取消 5已过期
     */
    @TableField("status")
    private Integer status;

    /**
     * 任务过期时间
     */
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 完成时间
     */
    @TableField("finish_time")
    private LocalDateTime finishTime;

    /**
     * 失败原因
     */
    @TableField("error_message")
    private String errorMessage;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
