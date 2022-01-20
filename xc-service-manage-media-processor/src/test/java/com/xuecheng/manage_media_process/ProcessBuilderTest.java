package com.xuecheng.manage_media_process;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 使用Runtime 或ProcessBuilder 调用第三方应用程序测试
 *
 * @author Kang Yong
 * @date 2022/1/20
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProcessBuilderTest {

    /**
     * 测试使用 ProcessBuilder 调用第三方应用程序
     */
    @Test
    public void testProcessBuilderTest() throws IOException {
        // 创建对象 ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 设置第三方程序的命令
        processBuilder.command("ping", "127.0.0.1");

        // 将标准输入流和错误流合并
        processBuilder.redirectErrorStream(true);
        // 启动一个进程
        Process process = processBuilder.start();

        // 通过标准输入流来拿到正常和错误的信息
        InputStream inputStream = process.getInputStream();
        // 创建字符流，读取字节流
        InputStreamReader isr = new InputStreamReader(inputStream, "gbk");
        char[] chars = new char[1024];
        int len = -1;
        while ((len = isr.read(chars)) != -1) {
            String str = new String(chars, 0, len);
            System.out.println(str);
        }

        // 关闭流
        inputStream.close();
        isr.close();
    }

}
