package com.xuecheng.test.freemarker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * freemarker测试 控制层
 */
@Controller
@RequestMapping("/freemarker")
public class TestFreemarkerController {

    @GetMapping("/test01")
    public String freemarker(Map<String, Object> map) {
        map.put("name", "王丽丽");
        // 返回模板文件名称
        return "test01";
    }
}
