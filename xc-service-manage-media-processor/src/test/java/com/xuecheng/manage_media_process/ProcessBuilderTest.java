package com.xuecheng.manage_media_process;

import com.xuecheng.framework.utils.Mp4VideoUtil;
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
//        processBuilder.command("ping", "127.0.0.1");
        processBuilder.command("ipconfig");

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

    @Test
    public void testMp4VideoUtil() {
        // 准备参数
        String ffmpeg_path = "C:\\soft\\ffmpeg\\bin\\ffmpeg.exe";
        String video_path = "D:\\data\\video\\solr.avi";
        String mp4_name = "solr_test.mp4";
        String mp4folder_path = "D:\\data\\video\\";
        // 创建对象
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4folder_path);
        // 生成MP4文件
        String mp4 = mp4VideoUtil.generateMp4();
        System.out.println(mp4);
    }

}
