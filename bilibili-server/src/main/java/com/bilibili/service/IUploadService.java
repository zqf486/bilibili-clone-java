package com.bilibili.service;

import com.bilibili.dto.UploadInitDTO;

public interface IUploadService {

    /**
     * 上传初始化
     */
    void init(UploadInitDTO dto);
}
