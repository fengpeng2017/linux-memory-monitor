package com.jthinking.monitor.web.listener;

import com.jthinking.monitor.util.ClasspathProperties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 设置Web应用全局变量
 * @author JiaBochao
 * @version 2017-11-17 10:25:27
 */
@WebListener
public class GlobalVariableServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ClasspathProperties properties = new ClasspathProperties("classpath:applicationVariable.properties");
            Set<Map.Entry<Object, Object>> entries = properties.entrySet();
            for (Map.Entry<Object, Object> entry : entries) {
                servletContextEvent.getServletContext().setAttribute((String) entry.getKey(), entry.getValue());
            }

            //MemoryListener.startListen();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //MemoryListener.stopListen();
    }
}
