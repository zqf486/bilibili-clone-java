package com.bilibili.context;

import com.bilibili.vo.UserLoginVO;

public class UserContext {

    private static final ThreadLocal<UserLoginVO> CONTEXT = new ThreadLocal<>();

    public static void set(UserLoginVO user) {
        CONTEXT.set(user);
    }

    public static UserLoginVO get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
