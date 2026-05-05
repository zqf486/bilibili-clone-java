package com.bilibili.service;

import com.bilibili.dto.UploadCheckDTO;
import com.bilibili.vo.VideoCheckVO;

public interface IFileService {

    /**
     * 视频文件上传检查
     *
     * @param uploadCheckDTO 文件md5
     * @return 三个状态 ["FINISHED", "UPLOADING", "NEW"]
     */
    VideoCheckVO check(UploadCheckDTO uploadCheckDTO);
}
