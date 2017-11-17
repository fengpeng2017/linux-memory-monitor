package com.jthinking.monitor.util;


import com.jthinking.monitor.pojo.User;

/**
 * 用户登录信息传递。实现线程安全
 * @author JiaBochao
 * @version 2017-11-17 10:23:33
 */
public class UserThreadLocal {

    private static final ThreadLocal<User> USER = new ThreadLocal<User>();

    public static void set(User user) {
        USER.set(user);
    }

    public static User get() {
        return USER.get();
    }

    public static String getUsername() {
        if (null != USER) {
            if (null != USER.get()) {
                return USER.get().getUsername();
            }
        }
        return null;
    }
}

