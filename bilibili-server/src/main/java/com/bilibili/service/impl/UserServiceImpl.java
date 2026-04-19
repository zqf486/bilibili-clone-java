package com.bilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.cache.CacheClient;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.context.UserContext;
import com.bilibili.dto.LoginDTO;
import com.bilibili.dto.RegisterDTO;
import com.bilibili.entity.TbUser;
import com.bilibili.enumeration.UserStatusEnum;
import com.bilibili.exception.AuthException;
import com.bilibili.exception.BusinessException;
import com.bilibili.mapper.UserMapper;
import com.bilibili.service.ICheckCodeService;
import com.bilibili.service.IUserService;
import com.bilibili.util.RedisUtil;
import com.bilibili.vo.UserInfoVO;
import com.bilibili.vo.UserLoginVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CacheClient cacheClient;

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
                .updateTime(LocalDateTime.now())
                .build();

        // 4.保存
        userMapper.insert(user);
    }

    /**
     * 用户登录
     *
     * @param loginDTO
     * @return
     */
    @Override
    public UserLoginVO login(LoginDTO loginDTO) {

        // 0.检查验证码
        checkCodeService.verifyCheckCode(loginDTO.getCheckCodeKey(), loginDTO.getCheckCode());

        // 1.判断账户是否存在
        boolean exists = lambdaQuery()
                .eq(TbUser::getEmail, loginDTO.getEmail())
                .count() > 0;
        if (!exists) {
            throw new BusinessException(MessageConstant.EMAIL_ACCOUNT_ALREADY_EXISTED);
        }

        // 2.判断秘密是否正确
        TbUser user = lambdaQuery()
                .eq(TbUser::getEmail, loginDTO.getEmail())
                .one();
        String salt = user.getSalt();
        String encryptPwd = DigestUtil.md5Hex(loginDTO.getPassword() + salt);
        if (!user.getPassword().equals(encryptPwd)) {
            throw new BusinessException(MessageConstant.ACCOUNT_OR_PASSWORD_NOT_EXISTED);
        }

        // 3.判断账户是否被禁用
        if (UserStatusEnum.DISABLED.getCode() == user.getStatus()) {
            throw new BusinessException(MessageConstant.ACCOUNT_BANNED);
        }

        // 4. 设置最后登录时间
        userMapper.update(
                null,
                new LambdaUpdateWrapper<TbUser>()
                        .eq(TbUser::getId, user.getId())
                        .set(TbUser::getLastLoginTime, LocalDateTime.now())
        );

        // 5. 构建返回对象
        String token = IdUtil.fastSimpleUUID();
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .build();

        // 6. 保存返回对象到 redis
        redisUtil.set(RedisConstant.REDIS_TOKEN_KEY_WEB, token, userLoginVO, RedisConstant.REDIS_KEY_EXPIRES_ONE_DAY);

        return userLoginVO;
    }

    /**
     * 用户退出
     */
    @Override
    public void logout() {
        // 0.获取 UserLoginVO
        UserLoginVO userLoginVO = UserContext.get();
        if (userLoginVO == null) {
            throw new AuthException(MessageConstant.USER_NOT_LOGIN);
        }

        // 1.清除 redis 登录状态
        redisUtil.delete(RedisConstant.REDIS_TOKEN_KEY_WEB, userLoginVO.getToken());
    }

    /**
     * 获取用户信息
     * TODO: 粉丝数量, 关注列表等
     *
     * @param id
     * @return 用户信息
     */
    @Override
    public UserInfoVO getUserById(Long id) {
        TbUser tbUser = cacheClient.queryWithPassThrough(RedisConstant.CACHE_USER_KEY, id, TbUser.class, userMapper::selectById, RedisConstant.REDIS_KEY_EXPIRES_ONE_MIN * 30);
        UserInfoVO userInfoVO = BeanUtil.copyProperties(tbUser, UserInfoVO.class);
        return userInfoVO;
    }
}
