package com.bilibili.controller.web;

import com.bilibili.annotation.LoginRequired;
import com.bilibili.dto.UploadInitDTO;
import com.bilibili.result.Result;
import com.bilibili.service.IVideoTaskService;
import com.bilibili.vo.UploadTaskVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "VideoUploadController")
@Slf4j
@RestController
@RequestMapping("/api/video/upload")
public class VideoUploadController {

    @Resource
    private IVideoTaskService videoTaskService;

    /**
     * 初始化上传任务
     *
     * @param dto 视频 md5 视频时长
     * @return
     */
    @Operation(summary = "初始化上传任务")
    @LoginRequired
    @PostMapping("/init")
    public Result<UploadTaskVO> init(@RequestBody UploadInitDTO dto) {
        return Result.success(videoTaskService.init(dto));
    }

    /**
     * 上传分片
     *
     * @param taskId     上传任务id
     * @param partNumber 上传分片号
     * @param file       分片文件
     * @return
     */
    @Operation(summary = "上传分片")
    @LoginRequired
    @PostMapping("/{taskId}/chunk")
    public Result uploadChunk(
            @PathVariable Long taskId,
            @RequestParam Integer partNumber,
            @RequestParam MultipartFile file
    ) {
        videoTaskService.uploadChunk(taskId, partNumber, file);
        return Result.success();
    }
}
