package com.jthinking.monitor.service;

import com.jthinking.monitor.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录管理
 * @author JiaBochao
 * @version 2017-11-17 10:03:44
 */
public interface LoginManager {

    /**
     * 获取客户端用户信息
     * @param request
     * @return
     */
    User getLoginInfo(HttpServletRequest request);

    /**
     * 检查用户输入
     * @param email
     */
    void checkUserInput(String email);

    /**
     * 通过邮箱获取用户信息
     * @param email
     * @return
     */
    User getUserInfo(String email);

    /**
     * 发送动态登录密码
     * @param email
     * @return
     */
    String sendDynamicPassword(String email);

    /**
     * 验证登录
     * @param password
     * @param key
     * @return
     */
    boolean auth(String password, String key);

}
