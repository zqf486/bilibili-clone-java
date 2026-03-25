package com.bilibili.service;

import com.bilibili.dto.LoginDTO;
import com.bilibili.dto.RegisterDTO;
import com.bilibili.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bilibili.vo.UserInfoVO;
import com.bilibili.vo.UserLoginVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zqf486
 * @since 2026-03-20
 */
public interface IUserService extends IService<TbUser> {

    /**
     * 用户注册
     *
     * @param registerDTO
     */
    void register(@Valid RegisterDTO registerDTO);

    /**
     * 用户登录
     *
     * @param loginDTO
     * @return
     */
    UserLoginVO login(@Valid LoginDTO loginDTO);

    /**
     * 用户退出
     */
    void logout();

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    UserInfoVO getUserById(Long id);
}
