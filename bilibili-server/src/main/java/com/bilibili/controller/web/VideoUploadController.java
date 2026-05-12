package com.bilibili.controller.web;

import com.bilibili.annotation.LoginRequired;
import com.bilibili.dto.UploadInitDTO;
import com.bilibili.result.Result;
import com.bilibili.service.IVideoUploadService;
import com.bilibili.vo.UploadInitVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VideoUploadController")
@Slf4j
@RestController
@RequestMapping("/api/upload/")
public class VideoUploadController {

    @Resource
    private IVideoUploadService videoUploadService;

    /**
     * 初始化上传任务
     *
     * @param dto 视频 md5 视频时长
     * @return 预签名上传链接
     */
    @Operation(summary = "初始化上传任务")
    @LoginRequired
    @PostMapping("/init")
    public Result<UploadInitVO> init(@RequestBody UploadInitDTO dto) {
        UploadInitVO init = videoUploadService.init(dto);
        return Result.success(init);
    }

    /**
     * 上传心跳
     * 续期task的ttl防止被清除
     *
     * @param taskId
     * @return
     */
    @Operation(summary = "上传心跳（续期ttl）")
    @LoginRequired
    @PostMapping("/{taskId}/heartbeat")
    public Result heartbeat(@PathVariable Long taskId) {
        videoUploadService.heartbeat(taskId);
        return Result.success();
    }
}
