package com.jthinking.monitor.service;


import com.jthinking.monitor.pojo.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录管理实现类，使用Session存储用户登录信息
 * @author JiaBochao
 * @version 2017-11-17 10:08:30
 */
@Service("sessionLoginManager")
public class SessionLoginManager extends AbstractLoginManager {

    @Override
    public User getLoginInfo(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        } else {
            return user;
        }
    }


}
