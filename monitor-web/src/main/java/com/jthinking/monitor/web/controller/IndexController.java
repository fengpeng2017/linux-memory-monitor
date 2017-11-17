package com.jthinking.monitor.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 * @author JiaBochao
 * @version 2017-9-26 08:33:39
 */
@Controller
public class IndexController {

    @RequestMapping({"", "/", "/index"})
    public String index() {
        return "redirect:/user/login";
    }
}
