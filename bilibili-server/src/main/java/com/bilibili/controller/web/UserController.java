package com.bilibili.controller.web;

import com.bilibili.dto.RegisterDTO;
import com.bilibili.result.Result;
import com.bilibili.service.ICheckCodeService;
import com.bilibili.service.IUserService;
import com.bilibili.vo.CheckCodeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zqf486
 * @since 2026-03-20
 */
@Tag(name = "UserController")
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private ICheckCodeService checkCodeService;

    /**
     * 获取图片验证码
     *
     * @return
     */
    @Operation(summary = "获取图片验证码")
    @GetMapping("/getCheckCode")
    public Result<CheckCodeVO> getCheckCode() {
        CheckCodeVO checkCodeVO = checkCodeService.getCheckCode();
        return Result.success(checkCodeVO);
    }

    /**
     * 用户注册
     *
     * @param registerDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }

}
