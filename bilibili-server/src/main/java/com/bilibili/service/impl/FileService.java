package com.bilibili.service.impl;

import com.bilibili.dto.UploadCheckDTO;
import com.bilibili.service.IFileService;
import com.bilibili.service.IVideoTaskService;
import com.bilibili.vo.VideoCheckVO;
import jakarta.annotation.Resource;

public class FileService implements IFileService {

    @Resource
    private IVideoTaskService videoTaskService;

    /**
     * 视频文件上传检查
     *
     * @param uploadCheckDTO 文件 md5
     * @return 三个状态 ["FINISHED", "UPLOADING", "NEW"]
     */
    @Override
    public VideoCheckVO check(UploadCheckDTO uploadCheckDTO) {
        // 1. 查询 tb_video_task md5: 若存在则返回上传进度 UPLOADING; 不存在则建立新记录并返回 NEW
        videoTaskService.init(uploadCheckDTO);
        return null;
    }
}
