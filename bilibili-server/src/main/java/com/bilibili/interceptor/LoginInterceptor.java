package com.bilibili.interceptor;

import com.bilibili.annotation.LoginRequired;
import com.bilibili.constant.CookieConstant;
import com.bilibili.constant.MessageConstant;
import com.bilibili.constant.RedisConstant;
import com.bilibili.context.UserContext;
import com.bilibili.exception.AuthException;
import com.bilibili.util.RedisUtil;
import com.bilibili.vo.UserLoginVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 0.非controller 请求直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;

        // 1.判断是否需要登录
        boolean needLogin = method.hasMethodAnnotation(LoginRequired.class)
                || method.getBeanType().isAnnotationPresent(LoginRequired.class);

        if (!needLogin) {
            return true;
        }

        // 2.获取 cookie中的token
        String token = getTokenFromCookie(request);

        if (token == null) {
            throw new AuthException(MessageConstant.USER_NOT_LOGIN);
        }

        // 2.1.获取 redis 登录状态
        String key = RedisConstant.REDIS_TOKEN_KEY_WEB + token;
        UserLoginVO user = redisUtil.get(key, UserLoginVO.class);

        if (user == null) {
            throw new AuthException(MessageConstant.USER_NOT_LOGIN);
        }

        // 3.存 ThreadLocal
        UserContext.set(user);

        // 2.滑动过期
        Long ttl = redisUtil.getExpire(key);
        log.info("ttl: {}", ttl);
        log.info("ttl2: {}", RedisConstant.REDIS_KEY_EXPIRES_ONE_DAY / 2);
        if (ttl != null && ttl > 0 && ttl < RedisConstant.REDIS_KEY_EXPIRES_ONE_DAY / 2) {
            redisUtil.expire(key, RedisConstant.REDIS_KEY_EXPIRES_ONE_DAY);
            // 2.1.同步刷新cookie
            refreshCookie(response, token);
        }

        return true;
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
