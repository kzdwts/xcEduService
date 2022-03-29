package com.xuecheng.auth.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * SpringSecurity密码测试
 *
 * @author Kang Yong
 * @date 2022/2/7
 * @since 1.0.0
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class PasswordTest {

    @Test
    public void testPasswordEncoder() {
        String password = "111111";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 10; i++) {
            // 每个计算出的hash值都不一样
            String hashPass = passwordEncoder.encode(password);
            System.out.println(hashPass);

            // 虽然每次计算的密码Hash值都不一样，但是校验是通过的
            boolean b = passwordEncoder.matches(password, hashPass);
            System.out.println(b);
        }
    }

    @Test
    public void testPassword() {
        for (int i = 0; i < 10; i++) {
            String pwd = BCrypt.hashpw("111111", BCrypt.gensalt());
            System.out.println("密码：" + pwd);

            boolean checkpw = BCrypt.checkpw("111111", pwd);
            System.out.println(checkpw);
        }
        System.out.println("===GAME===OVER===");
    }
}
