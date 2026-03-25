package com.bilibili.controller.web;

import com.bilibili.annotation.LoginRequired;
import com.bilibili.context.UserContext;
import com.bilibili.dto.LoginDTO;
import com.bilibili.dto.RegisterDTO;
import com.bilibili.result.Result;
import com.bilibili.service.ICheckCodeService;
import com.bilibili.service.IUserService;
import com.bilibili.util.CookieUtil;
import com.bilibili.vo.CheckCodeVO;
import com.bilibili.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        log.info("获取图片验证码");
        CheckCodeVO checkCodeVO = checkCodeService.getCheckCode();
        return Result.success(checkCodeVO);
    }

    /**
     * 用户注册
     *
     * @param registerDTO
     * @return
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterDTO registerDTO) {
        log.info("用户注册: {}", registerDTO);
        userService.register(registerDTO);
        return Result.success();
    }

    /**
     * 用户登录
     *
     * @param response
     * @param loginDTO
     * @return
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(HttpServletResponse response, @Valid @RequestBody LoginDTO loginDTO) {
        log.info("用户登录： {}", loginDTO);
        UserLoginVO userLoginVO = userService.login(loginDTO);
        String token = userLoginVO.getToken();
        CookieUtil.saveToken2Cookie(response, token);
        return Result.success(userLoginVO);
    }


    /**
     * 用户退出
     *
     * @param response
     * @return
     */
    @LoginRequired
    @Operation(summary = "用户退出")
    @PostMapping("/logout")
    public Result logout(HttpServletResponse response) {
        log.info("用户退出: {}", UserContext.get());
        userService.logout();
        CookieUtil.cleanToken2Cookie(response);
        return Result.success();
    }
}
