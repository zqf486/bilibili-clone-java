package com.bilibili.util;

import com.bilibili.constant.CookieConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * 保存 token 到 Cookie
     *
     * @param response
     */
    public static void saveToken2Cookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(CookieConstant.TOKEN_KEY, token);
        cookie.setMaxAge(CookieConstant.COOKIE_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 清除前端 cookie 中的 token
     *
     * @param response
     */
    public static void cleanToken2Cookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(CookieConstant.TOKEN_KEY, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
