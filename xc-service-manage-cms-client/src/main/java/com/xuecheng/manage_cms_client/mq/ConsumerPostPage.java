package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms_client.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消费者
 *
 * @author Kang Yong
 * @date 2021/12/7
 * @since 1.0.0
 */
@Slf4j
@Component
public class ConsumerPostPage {

    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        // 接受数据
        Map map = JSON.parseObject(msg, Map.class);
        String pageId = (String) map.getOrDefault("pageId", null);
        if (StringUtils.isEmpty(pageId)) {
            log.error("参数不能Wie空");
        }

        // 生成页面到服务器
        this.pageService.savePageToServerPath(pageId);
    }

}
