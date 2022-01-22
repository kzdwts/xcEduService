package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 媒体资源mq消息处理
 *
 * @author Kang Yong
 * @date 2022/1/22
 * @since 1.0.0
 */
@Slf4j
@Component
public class MediaProcessTask {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    // ffmpeg地址
    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpegPath;

    // 上传文件根目录
    @Value("${xc-service-manage-media.upload-location}")
    private String uploadLocation;

    /**
     * 接收视频处理消息进行视频处理
     *
     * @param msg
     */
    @RabbitListener(queues = "${xc-service-manage-media.mq.queue-media-video-processor}", containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg) {
        log.info("===处理视频文件===START===");
        // 1、解析消息内容得到mediaId
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String) map.get("mediaId");

        // 2、根据mediaId查询文件信息
        Optional<MediaFile> mediaFileOptional = this.mediaFileRepository.findById(mediaId);
        if (!mediaFileOptional.isPresent()) {
            log.info("没有查询到资源文件信息");
            return;
        }
        MediaFile mediaFile = mediaFileOptional.get();

        // 根据文件类型判断文件是否需要处理
        String fileType = mediaFile.getFileType();
        if (!fileType.equals("avi")) {
            // ffmpeg目前只处理avi视频，其它视频暂不处理
            mediaFile.setProcessStatus("303004"); // 无需处理
            this.mediaFileRepository.save(mediaFile);
            return;
        } else {
            mediaFile.setProcessStatus("303001");
            this.mediaFileRepository.save(mediaFile);
        }

        // 3、调用工具类将avi文件转为mp4
        // 要处理的MP4的路径
        String videoPath = uploadLocation + mediaFile.getFilePath() + mediaFile.getFileName();
        // 生成的MP4的文件名
        String mp4Name = mediaFile.getFileId() + ".mp4";
        // 生成的MP4所在的路径
        String mp4folderPath = uploadLocation + mediaFile.getFilePath();
        // 创建工具类对象
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath, videoPath, mp4Name, mp4folderPath);
        String mp4Result = mp4VideoUtil.generateMp4();
        if (mp4Result == null || !mp4Result.equals("success")) {
            // 处理失败
            log.info("avi转mp4处理失败");
            mediaFile.setProcessStatus("303003");
            // 记录失败原因
            MediaFileProcess_m3u8 mediaFileProcessM3u8 = new MediaFileProcess_m3u8();
            mediaFileProcessM3u8.setErrormsg(mp4Result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcessM3u8);

            this.mediaFileRepository.save(mediaFile);
            return;
        }

        // 4、将MP4转为m3u8和ts文件
        String mp4VideoPath = uploadLocation + mediaFile.getFilePath() + mp4Name;
        String m3u8Name = mediaFile.getFileId() + ".m3u8";
        String m3u8folderPath = uploadLocation + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpegPath, mp4VideoPath, m3u8Name, m3u8folderPath);
        String m3u8Result = hlsVideoUtil.generateM3u8();
        if (m3u8Result == null || !m3u8Result.equals("success")) {
            // 处理失败
            log.info("mp4转m3u8处理失败");
            mediaFile.setProcessStatus("303003");
            // 记录失败原因
            MediaFileProcess_m3u8 mediaFileProcessM3u8 = new MediaFileProcess_m3u8();
            mediaFileProcessM3u8.setErrormsg(m3u8Result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcessM3u8);

            this.mediaFileRepository.save(mediaFile);
            return;
        }

        // 处理成功
        mediaFile.setProcessStatus("303002");
        List<String> tsList = hlsVideoUtil.get_ts_list();
        MediaFileProcess_m3u8 mediaFileProcessM3u8 = new MediaFileProcess_m3u8();
        mediaFileProcessM3u8.setTslist(tsList);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcessM3u8);
        // fileUrl 就是客户端访问文件的路径
        String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8Name;
        this.mediaFileRepository.save(mediaFile);
        log.info("===处理视频文件===END===");
    }
}
