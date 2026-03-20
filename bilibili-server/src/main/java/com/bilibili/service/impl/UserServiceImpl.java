package com.bilibili.service.impl;

import com.bilibili.entity.TbUser;
import com.bilibili.mapper.UserMapper;
import com.bilibili.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zqf486
 * @since 2026-03-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, TbUser> implements IUserService {

}
