package com.bilibili.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadInitVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * task id
     */
    private Long taskId;

    /**
     * MinIO multipart upload ID
     */
    private String uploadId;

    /**
     * 分片大小（字节）
     */
    private Integer chunkSize;

    /**
     * 总分片数
     */
    private Integer totalChunks;

    /**
     * 已完成分片序号列表
     */
    private List<Integer> uploadedParts;
}
