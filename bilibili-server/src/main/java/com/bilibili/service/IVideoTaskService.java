package com.bilibili.service;

import com.bilibili.dto.UploadInitDTO;
import com.bilibili.vo.UploadTaskVO;
import org.springframework.web.multipart.MultipartFile;

public interface IVideoTaskService {

    /**
     * 初始化上传任务
     *
     * @param dto 视频 md5 视频时长
     * @return 上传任务id, 分片大小, 分片数量, 上传进度
     */
    UploadTaskVO init(UploadInitDTO dto);

    /**
     * 分片上传
     *
     * @param taskId     上传任务id
     * @param partNumber 分片序号
     * @param file       分片文件
     */
    void uploadChunk(Long taskId, Integer partNumber, MultipartFile file);
}
