package com.bilibili.service;

import com.bilibili.dto.UploadInitDTO;
import com.bilibili.vo.UploadInitVO;

import java.util.List;

public interface IVideoUploadService {

    /**
     * 分片上传初始化
     *
     * @return 预签名上传链接
     */
    UploadInitVO init(UploadInitDTO dto);

    /**
     * 心跳续期 task ttl
     */
    void heartbeat(Long taskId);
}
