package com.xuecheng.manage_course;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * ribbon测试
 *
 * @author Kang Yong
 * @date 2021/12/17
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void demo01TestRibbon() {
        // 确定要获取的服务名称
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        ResponseEntity<Map> entity = restTemplate.getForEntity("http://" + serviceId + "/cms/page/findById/5a754adf6abb500ad05688d9", Map.class);
        Map map = entity.getBody();
        System.out.println(map);
    }

}
