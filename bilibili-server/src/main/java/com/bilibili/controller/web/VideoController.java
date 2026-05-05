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
}
