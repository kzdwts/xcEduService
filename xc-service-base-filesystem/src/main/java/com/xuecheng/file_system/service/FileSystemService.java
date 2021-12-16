package com.xuecheng.file_system.service;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理业务层
 *
 * @author Kang Yong
 * @date 2021/12/16
 * @since 1.0.0
 */
public interface FileSystemService {

    /**
     * 上传文件
     *
     * @param file        文件
     * @param filetag     文件标签
     * @param businesskey 业务key
     * @param metadata    元信息，json格式
     * @return
     */
    UploadFileResult upload(MultipartFile file, String filetag, String businesskey, String metadata);

    /**
     * 上传文件到fdfs，返回文件id
     *
     * @param file 文件
     * @return
     */
    String fdfs_upload(MultipartFile file);
}
