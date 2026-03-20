package com.bilibili.service.impl;

import com.bilibili.service.ICheckCodeService;
import com.bilibili.vo.CheckCodeVO;
import org.springframework.stereotype.Service;

@Service
public class CheckCodeServiceImpl implements ICheckCodeService {
    @Override
    public CheckCodeVO getCheckCode() {
        return null;
    }

    @Override
    public void verifyCheckCode(String checkCodeKey, String checkCode) {

    }
}
