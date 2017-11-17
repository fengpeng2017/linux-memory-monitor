package com.jthinking.monitor.service;


import com.jthinking.monitor.pojo.User;
import com.jthinking.monitor.util.ClasspathProperties;
import com.jthinking.monitor.util.JavaMail;
import com.jthinking.monitor.web.exception.UserException;
import java.io.IOException;
import java.util.Random;

/**
 * 登录管理抽象实现类
 * @author JiaBochao
 * @version 2017-11-17 10:05:31
 */
public abstract class AbstractLoginManager implements LoginManager {

    @Override
    public void checkUserInput(String email) {
        if (email == null || email.trim().equals("")) {
            throw new UserException("邮箱不能为空");
        }
    }

    @Override
    public User getUserInfo(String email) {
        ClasspathProperties properties = null;
        try {
            properties = new ClasspathProperties("classpath:users.properties");
            String username = properties.getProperty(email);

            if (username == null) {
                throw new UserException("用户不存在");
            }
            return new User(email, username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendDynamicPassword(String email) {
        Random rd = new Random();
        String key = String.format("%06d", rd.nextInt(999999));
        JavaMail.send(email, "动态口令", "您正在登录超级管理员后台，口令为：" + key);
        return key;
    }

    @Override
    public boolean auth(String password, String key) {
        if (password == null) {
            throw new UserException("密码不能为空");
        }
        if (key == null) {
            throw new UserException("动态密码失效");
        }
        if (password.equals(key)) {
            return true;
        } else {
            return false;
        }
    }
}
