package com.bilibili.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    DISABLED(0, "禁用"),
    ENABLED(1, "正常");

    private final int code;
    private final String desc;
}
