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
public class VideoCheckVO implements Serializable {

    /**
     * 文件上传任务状态 ["FINISHED", "UPLOADING", "NEW"]
     */
    private String status;

    /**
     * 上传任务全局唯一id
     * 用于后续的【分片上传】和【合并请求】接口，作为凭证关联本次上传
     * 在 status 为 UPLOADING 或 NEW 时必返回
     */
    private String taskId;

    /**
     * 已经成功上传的分片序号列表
     * 只有在 status 为 UPLOADING (续传) 时才需要返回数据
     * 前端拿到后，会将这个列表里的序号从待上传列表中剔除
     */
    private List<Integer> uploadedChunks;

    /**
     * 视频最终的访问或存储链接
     * 只有在 status 为 FINISHED (秒传) 时才返回，直接告诉前端视频在哪
     */
    private String url;
}
