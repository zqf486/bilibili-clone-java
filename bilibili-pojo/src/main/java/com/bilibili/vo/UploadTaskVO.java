package com.bilibili.vo;

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
public class UploadTaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文件MD5
     */
    private String md5;

    /**
     * 文件总大小
     */
    private Long fileSize;

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
     * 前端通过 size() / totalChunks 算进度，通过列表内容判断哪些片跳过
     */
    private List<Integer> uploadedChunks;
}
