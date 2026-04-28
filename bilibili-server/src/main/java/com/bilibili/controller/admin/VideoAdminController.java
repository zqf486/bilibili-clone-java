package com.bilibili.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bilibili.annotation.LoginRequired;
import com.bilibili.result.Result;
import com.bilibili.service.IVideoService;
import com.bilibili.vo.VideoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VideoAdminController")
@Slf4j
@RestController
@RequestMapping("/admin/video")
public class VideoAdminController {

    @Resource
    private IVideoService videoService;

    @Operation(summary = "分页查询视频（管理端）")
    @GetMapping("/page")
    public Result<Page<VideoVO>> page(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Byte status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        return Result.success();
    }

    @Operation(summary = "审核视频")
    @LoginRequired
    @PutMapping("/{id}/status")
    public Result review(
            @PathVariable Long id,
            @RequestParam Byte status
    ) {
        return Result.success();
    }
}
