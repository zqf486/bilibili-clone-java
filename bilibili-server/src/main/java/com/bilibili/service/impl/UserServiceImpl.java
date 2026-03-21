package com.bilibili.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.TbUserConstant;
import com.bilibili.dto.RegisterDTO;
import com.bilibili.entity.TbUser;
import com.bilibili.exception.BusinessException;
import com.bilibili.mapper.UserMapper;
import com.bilibili.service.ICheckCodeService;
import com.bilibili.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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

    @Resource
    private UserMapper userMapper;

    @Resource
    private ICheckCodeService checkCodeService;

    /**
     * 用户注册
     *
     * @param registerDTO
     */
    @Override
    @Transactional
    public void register(RegisterDTO registerDTO) {

        // 0.检查验证码是否正确
        checkCodeService.verifyCheckCode(registerDTO.getCheckCodeKey(), registerDTO.getCheckCode());


        // 1.判断账户是否已经存在
        boolean exists = lambdaQuery()
                .eq(TbUser::getEmail, registerDTO.getEmail())
                .count() > 0;
        if (exists) {
            throw new BusinessException(MessageConstant.EMAIL_ACCOUNT_ALREADY_EXISTED);
        }

        // 2.判断用户名是否已经存在
        exists = lambdaQuery()
                .eq(TbUser::getNickname, registerDTO.getNickName())
                .count() > 0;
        if (exists) {
            throw new BusinessException(MessageConstant.NICK_NAME_ALREADY_EXISTED);
        }

        // 3.创建新用户ID (雪花算法)
        Long userId = IdUtil.getSnowflakeNextId();

        // 3.1.盐
        String salt = IdUtil.simpleUUID();

        // 3.2.密码加盐 md5
        String encryptPwd = DigestUtil.md5Hex(registerDTO.getRegisterPassword() + salt);

        TbUser user = TbUser.builder()
                .id(userId)
                .username(registerDTO.getNickName())
                .email(registerDTO.getEmail())
                .password(encryptPwd)
                .salt(salt)
                .createTime(LocalDateTime.now())
                .build();

        // 4.保存
        userMapper.insert(user);
    }
}
