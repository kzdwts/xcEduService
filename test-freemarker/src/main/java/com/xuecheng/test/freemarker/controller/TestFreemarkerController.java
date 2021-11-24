package com.xuecheng.test.freemarker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * freemarker测试 控制层
 */
@Controller
@RequestMapping("/freemarker")
public class TestFreemarkerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test01")
    public String freemarker(Map<String, Object> map) {
        map.put("name", "王丽丽");
        // 返回模板文件名称
        return "test01";
    }

    /**
     * 轮播图
     *
     * @param map
     * @return
     */
    @GetMapping("/banner")
    public String banner(Map<String, Object> map) {
        // 查询数据
        ResponseEntity<Map> entity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map body = entity.getBody();
        map.putAll(body);

        // 返回模板文件名称
        return "index_banner";
    }
}
