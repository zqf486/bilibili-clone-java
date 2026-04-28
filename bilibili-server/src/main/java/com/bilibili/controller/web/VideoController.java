package com.bilibili.controller.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bilibili.annotation.LoginRequired;
import com.bilibili.dto.VideoPublishDTO;
import com.bilibili.result.Result;
import com.bilibili.service.IVideoService;
import com.bilibili.util.MinioUtil;
import com.bilibili.vo.VideoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "VideoController")
@Slf4j
@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Resource
    private IVideoService videoService;

    @Resource
    private MinioUtil minioUtil;

    @Operation(summary = "分页查询视频列表")
    @GetMapping("/page")
    public Result<Page<VideoVO>> page(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        return Result.success();
    }

    @Operation(summary = "获取视频详情")
    @GetMapping("/{id}")
    public Result<VideoVO> detail(@PathVariable Long id) {
        return Result.success();
    }

    @Operation(summary = "获取热门视频")
    @GetMapping("/hot")
    public Result<List<VideoVO>> hot() {
        return Result.success();
    }

    @Operation(summary = "发布视频")
    @LoginRequired
    @PostMapping
    public Result publish(
            @Valid VideoPublishDTO publishDTO,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "cover", required = false) MultipartFile coverFile
    ) {
        return Result.success();
    }

    @Operation(summary = "删除视频")
    @LoginRequired
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success();
    }

    @Operation(summary = "点赞/取消点赞")
    @LoginRequired
    @PostMapping("/{id}/like")
    public Result like(@PathVariable Long id) {
        return Result.success();
    }

    @Operation(summary = "增加播放量")
    @PostMapping("/{id}/view")
    public Result addView(@PathVariable Long id) {
        return Result.success();
    }
}
