package com.bilibili.service.impl;

import com.bilibili.dto.UploadCheckDTO;
import com.bilibili.mapper.UploadTaskMapper;
import com.bilibili.service.IVideoTaskService;
import jakarta.annotation.Resource;

public class VideoTaskService implements IVideoTaskService {

    @Resource
    private UploadTaskMapper uploadTaskMapper;

    /**
     * 视频上传初始化
     *
     * @param uploadCheckDTO
     */
    @Override
    public void init(UploadCheckDTO uploadCheckDTO) {

    }
}
