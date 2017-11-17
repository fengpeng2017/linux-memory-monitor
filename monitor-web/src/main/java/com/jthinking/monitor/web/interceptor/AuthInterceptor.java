package com.jthinking.monitor.web.interceptor;

import com.jthinking.monitor.pojo.User;
import com.jthinking.monitor.service.LoginManager;
import com.jthinking.monitor.util.UserThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证
 * @author JiaBochao
 * @version 2017-11-17 10:24:24
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private LoginManager sessionLoginManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        User user = sessionLoginManager.getLoginInfo(request);
        String requestURL = request.getRequestURL().toString();

        if (user == null) {
            UserThreadLocal.set(null);
            response.sendRedirect("/user/login?service=" + requestURL);
            return false;
        } else {
            UserThreadLocal.set(user);
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
