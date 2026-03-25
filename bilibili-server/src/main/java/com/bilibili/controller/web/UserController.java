package com.bilibili.controller.web;

import com.bilibili.annotation.LoginRequired;
import com.bilibili.constant.MessageConstant;
import com.bilibili.context.UserContext;
import com.bilibili.dto.LoginDTO;
import com.bilibili.dto.RegisterDTO;
import com.bilibili.exception.AuthException;
import com.bilibili.result.Result;
import com.bilibili.service.ICheckCodeService;
import com.bilibili.service.IUserService;
import com.bilibili.util.CookieUtil;
import com.bilibili.vo.CheckCodeVO;
import com.bilibili.vo.UserInfoVO;
import com.bilibili.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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
    public Result<UserLoginVO> login(HttpServletResponse response, @Valid @RequestBody LoginDTO loginDTO) {
        log.info("用户登录： {}", loginDTO);
        UserLoginVO userLoginVO = userService.login(loginDTO);
        String token = userLoginVO.getToken();
        CookieUtil.saveToken2Cookie(response, token);
        return Result.success(userLoginVO);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return userLoginVO
     */
    @Operation(summary = "获取当前登录用户信息")
    @LoginRequired
    @GetMapping("/me")
    public Result<UserLoginVO> me() {
        UserLoginVO userLoginVO = UserContext.get();
        if (userLoginVO == null) {
            throw new AuthException(MessageConstant.USER_NOT_LOGIN);
        }
        return Result.success(userLoginVO);
    }

    /**
     * 用户退出
     *
     * @param response
     * @return
     */
    @Operation(summary = "用户退出")
    @LoginRequired
    @PostMapping("/logout")
    public Result logout(HttpServletResponse response) {
        log.info("用户退出: {}", UserContext.get());
        userService.logout();
        CookieUtil.cleanToken2Cookie(response);
        return Result.success();
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        log.info("获取用户信息: {}", id);
        UserInfoVO user = userService.getUserById(id);
        return Result.success(user);
    }
}
