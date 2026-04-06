package com.bilibili.interceptor;

import com.bilibili.constant.CookieConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.context.UserContext;
import com.bilibili.util.RedisUtil;
import com.bilibili.vo.UserLoginVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 0.非controller 请求直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 1.获取cookie 中的 token
        String token = getTokenFromCookie(request);

        // 1.1. token不存在，直接放行
        if (token == null) {
            return true;
        }

        UserLoginVO user = (UserLoginVO) redisUtil.get(RedisConstant.REDIS_TOKEN_KEY_WEB, token, UserLoginVO.class);

        // 2. token 过期，直接放行
        if (user == null) {
            return true;
        }

        // 3. 存 ThreadLocal
        UserContext.set(user);

        // 4.滑动过期
        Long ttl = redisUtil.getExpire(RedisConstant.REDIS_TOKEN_KEY_WEB, token);
        if (ttl != null && ttl > 0 && ttl < RedisConstant.REDIS_KEY_EXPIRES_ONE_DAY / 2) {
            redisUtil.expire(RedisConstant.REDIS_TOKEN_KEY_WEB, token, RedisConstant.REDIS_KEY_EXPIRES_ONE_DAY);
            // 4.1.同步刷新cookie
            refreshCookie(response, token);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 移除用户
        UserContext.clear();
    }

    /**
     * 从 request中拿去 cookie 并读取token
     *
     * @param request
     * @return
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (CookieConstant.TOKEN_KEY.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 刷新 请求客户端的cookie过期时间
     *
     * @param response
     * @param token
     */
    private void refreshCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(CookieConstant.TOKEN_KEY, token);
        cookie.setMaxAge(CookieConstant.COOKIE_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
