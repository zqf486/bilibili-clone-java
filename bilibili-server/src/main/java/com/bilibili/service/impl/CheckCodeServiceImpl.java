package com.bilibili.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.bilibili.constant.CheckCodeConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.exception.BusinessException;
import com.bilibili.service.ICheckCodeService;
import com.bilibili.util.RedisUtil;
import com.bilibili.vo.CheckCodeVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CheckCodeServiceImpl implements ICheckCodeService {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 生成图片验证码
     * 将验证码答案结果存入redis并返回redis key
     *
     * @return checkCodeVO
     */
    @Override
    public CheckCodeVO getCheckCode() {

        // 1.生成图片验证码
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(CheckCodeConstant.WIDTH, CheckCodeConstant.HEIGHT);

        // 2.生成图片验证码key 并存入redis
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtil.set(RedisConstant.REDIS_KEY_CHECK_CODE + checkCodeKey, shearCaptcha.getCode(), RedisConstant.REDIS_KEY_EXPIRES_ONE_MIN * 2);

        // 3.生成返回对象
        return CheckCodeVO.builder()
                .checkCodeKey(checkCodeKey)
                .checkCode(shearCaptcha.getImageBase64Data())
                .build();
    }

    /**
     * 验证码校验
     * 校验不通过 throw error
     * 无论校验通不通过都 clean redis key
     *
     * @param checkCodeKey redis key
     * @param checkCode    redis value
     */
    @Override
    public void verifyCheckCode(String checkCodeKey, String checkCode) {

        // 1.获取原始 checkCode
        String checkCodeRow = redisUtil.get(RedisConstant.REDIS_KEY_CHECK_CODE + checkCodeKey, String.class);
        if (StrUtil.isBlank(checkCodeRow)) {
            throw new BusinessException(MessageConstant.CHECK_CODE_EXPIRED);
        }

        // 2.clean redis key
        redisUtil.delete(RedisConstant.REDIS_KEY_CHECK_CODE + checkCodeKey);

        // 3.比对结果
        if (!checkCodeRow.equals(checkCode)) {
            throw new BusinessException(MessageConstant.CHECK_CODE_ERROR);
        }
    }
}
