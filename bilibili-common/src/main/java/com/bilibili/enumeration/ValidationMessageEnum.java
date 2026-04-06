package com.bilibili.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationMessageEnum {
    REGISTER_PASSWORD("registerPassword", "密码必须至少8位，包含大写字母、小写字母、数字和特殊符号");

    private final String errorField;
    private final String errorMsg;
}
