package com.jthinking.monitor.web.controller;

import com.jthinking.monitor.service.ServerMonitor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Tomcat集群监控，服务器内存、CPU占用率
 * 启动、停止、重启Tomcat
 * @author JiaBochao
 * @version 2017-9-26 08:21:32
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController {

    @RequestMapping("/page")
    public String page() {
        return "monitor-server";
    }

    @RequestMapping("/memory")
    @ResponseBody
    public String memory() {
        if (!ServerMonitor.isConnected()) {
            ServerMonitor.startListen();
        }
        return "success";
    }

}
