package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

/**
 * redis测试
 *
 * @author Kang Yong
 * @date 2022/2/2
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        // 定义key
        String key = "user_token:111";
        // 定义map
        Map<String, String> mapValue = new HashMap<>();
        mapValue.put("id", "101");
        mapValue.put("username", "itcast");
        String value = JSON.toJSONString(mapValue);
        stringRedisTemplate.boundValueOps(key).set(value, 60, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire(key);
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
    }
}
