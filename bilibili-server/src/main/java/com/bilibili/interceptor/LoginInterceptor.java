package com.bilibili.interceptor;

import com.bilibili.annotation.LoginRequired;
import com.bilibili.constant.MessageConstant;
import com.bilibili.context.UserContext;
import com.bilibili.exception.AuthException;
import com.bilibili.vo.UserLoginVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

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

        // 2.获取 ThreadLocal 中的用户信息
        UserLoginVO user = UserContext.get();

        if (user == null) {
            throw new AuthException(MessageConstant.USER_NOT_LOGIN);
        }

        return true;
    }
}
