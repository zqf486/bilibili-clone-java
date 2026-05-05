package com.bilibili.controller.web;

import com.bilibili.dto.UploadCheckDTO;
import com.bilibili.result.Result;
import com.bilibili.service.IFileService;
import com.bilibili.vo.VideoCheckVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "VideoUploadController")
@Slf4j
@RestController
@RequestMapping("/api/video/upload")
public class VideoUploadController {

    @Resource
    private IFileService fileService;

    /**
     * 接收视频文件md5，检测上传进度
     *
     * @param uploadCheckDTO 视频md5
     * @return
     */
    @PostMapping("/check")
    public Result check(@RequestBody UploadCheckDTO uploadCheckDTO) {
        VideoCheckVO videoCheckVO = fileService.check(uploadCheckDTO);
        return Result.success();
    }

}
