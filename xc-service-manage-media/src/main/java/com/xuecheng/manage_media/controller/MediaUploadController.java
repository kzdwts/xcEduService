package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 媒资文件管理 控制层
 *
 * @author Kang Yong
 * @date 2022/1/19
 * @since 1.0.0
 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    private MediaUploadService mediaUploadService;

    /**
     * 文件上传注册
     *
     * @param fileMd5  {@link String} 文件md5值（唯一标识）
     * @param fileName {@link String} 文件名称
     * @param fileSize {@link Long} 文件大小
     * @param mimetype {@link String} 文档类型
     * @param fileExt  {@link String} 文件扩展名
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2022/1/19
     */
    @Override
    @PostMapping("/register")
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return this.mediaUploadService.register(fileMd5, fileName, fileSize, mimetype, fileExt);
    }

    /**
     * 分块检查
     *
     * @param fileMd5   {@link String} 文件md5值（唯一标识）
     * @param chunk     {@link Integer} 当前分块下标
     * @param chunkSize {@link String} 当前分块大小
     * @return {@link CheckChunkResult}
     * @author Kang Yong
     * @date 2022/1/19
     */
    @Override
    @PostMapping("/checkchunk")
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return this.mediaUploadService.checkchunk(fileMd5, chunk, chunkSize);
    }

    /**
     * 上传分块
     *
     * @param file    {@link MultipartFile} 文件
     * @param chunk   {@link Integer}  当前分块下标
     * @param fileMd5 {@link String} 文件md5值（唯一标识）
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2022/1/19
     */
    @Override
    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunk(@RequestParam("file") MultipartFile file, Integer chunk, String fileMd5) {
        return this.mediaUploadService.uploadchunk(file, chunk, fileMd5);
    }

    /**
     * 合并文件分块
     *
     * @param fileMd5  {@link String} 文件md5值（唯一标识）
     * @param fileName {@link String} 文件名称
     * @param fileSize {@link Long} 文件大小
     * @param mimetype {@link String} 文档类型
     * @param fileExt  {@link String} 文件扩展名
     * @return {@link ResponseResult}
     * @author Kang Yong
     * @date 2022/1/19
     */
    @Override
    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return this.mediaUploadService.mergechunks(fileMd5, fileName, fileSize, mimetype, fileExt);
    }
}
