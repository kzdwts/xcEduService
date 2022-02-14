package com.xuecheng.order.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.service.XcTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

    @Autowired
    private XcTaskService xcTaskService;

    /**
     * 定时发送选课任务
     *
     * @author Kang Yong
     * @date `2022/2/13`
     */
//    @Scheduled(cron = "0/3 * * * * *")
    @Scheduled(cron = "0 3 * * * *")
    public void sendChooseCourseTask() {
        log.info("===选课mq消息发送===START===");

        // 时间
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, -1); // 前一分钟
        Date time = calendar.getTime();
        // 查询
        List<XcTask> xcTaskList = this.xcTaskService.findXcTaskList(time, 10);
//        System.out.println("xcTaskList.size()" + xcTaskList.size());
//        xcTaskList.forEach(System.out::println);
        if (!CollectionUtils.isEmpty(xcTaskList)) {
            for (XcTask xcTask : xcTaskList) {
                // 获取任务
                if (this.xcTaskService.getTask(xcTask.getId(), xcTask.getVersion()) > 0) {
                    String exchange = xcTask.getMqExchange();
                    String routingkey = xcTask.getMqRoutingkey();
                    this.xcTaskService.publish(xcTask, exchange, routingkey);
                    log.info("===发送了mq消息===ing===:{}", JSON.toJSONString(xcTask));
                }
            }
        }

        log.info("===选课mq消息发送===END===");
    }

    //    @Scheduled(fixedRate = 3000) // 在任务开始后3秒执行下一次调度
//    @Scheduled(fixedDelay = 3000) // 在任务结束后3秒后执行下一次调度
//    @Scheduled(cron = "0/3 * * * * *") // 每隔3秒去执行
    public void test01() {
        log.info("===选课mq消息发送===START1===");

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("===选课mq消息发送===END1===");
    }

    //    @Scheduled(fixedRate = 3000) // 在任务开始后3秒执行下一次调度
//    @Scheduled(fixedDelay = 3000) // 在任务结束后3秒后执行下一次调度
//    @Scheduled(cron = "0/3 * * * * *") // 每隔3秒去执行
    public void test02() {
        log.info("===选课mq消息发送===START2===");

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("===选课mq消息发送===END2===");
    }
}
