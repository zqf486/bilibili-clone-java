package com.bilibili.service;

import com.bilibili.dto.UploadCheckDTO;

public interface IVideoTaskService {

    /**
     * 视频上传初始化
     *
     * @param uploadCheckDTO
     */
    void init(UploadCheckDTO uploadCheckDTO);
}
