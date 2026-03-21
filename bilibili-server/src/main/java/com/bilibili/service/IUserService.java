package com.bilibili.service;

import com.bilibili.dto.RegisterDTO;
import com.bilibili.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
