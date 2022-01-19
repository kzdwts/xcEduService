package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 媒资文件管理 业务层
 *
 * @author Kang Yong
 * @date 2022/1/19
 * @since 1.0.0
 */
public interface MediaUploadService {

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
    ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

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
    CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize);

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
    ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5);

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
    ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);
}
