package com.bilibili.service;


import com.bilibili.vo.CheckCodeVO;

public interface ICheckCodeService {

    /**
     * 生成图片验证码
     * 将验证码答案结果存入redis并返回redis key
     *
     * @return checkCodeVO
     */
    CheckCodeVO getCheckCode();

    /**
     * 验证码校验
     * 校验不通过 throw error
     * 无论校验通不通过都 clean redis
     *
     * @param checkCodeKey redis key
     * @param checkCode redis value
     */
    void verifyCheckCode(String checkCodeKey, String checkCode);

}
