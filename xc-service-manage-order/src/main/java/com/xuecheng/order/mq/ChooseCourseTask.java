package com.xuecheng.order.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 选课任务
 *
 * @author Kang Yong
 * @date 2022/2/13
 * @since 1.0.0
 */
@Slf4j
@Component
public class ChooseCourseTask {

    @Scheduled(fixedRate = 3000) // 在任务开始后3秒执行下一次调度
//    @Scheduled(fixedDelay = 3000) // 在任务结束后3秒后执行下一次调度
//    @Scheduled(cron = "0/3 * * * * *") // 每隔3秒去执行
    public void test01() {
        log.info("===选课mq消息发送===START===");

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("===选课mq消息发送===END===");
    }
}
