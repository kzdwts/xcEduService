package com.xuecheng.manage_media_process.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * 媒体资源mq消息处理
 *
 * @author Kang Yong
 * @date 2022/1/22
 * @since 1.0.0
 */
public class MediaProcessTask {

    /**
     * 接收视频处理消息进行视频处理
     *
     * @param msg
     */
    @RabbitListener(queues = "${xc-service-manage-media.queue-media-video-processor}")
    public void receiveMediaProcessTask(String msg) {

    }
}
