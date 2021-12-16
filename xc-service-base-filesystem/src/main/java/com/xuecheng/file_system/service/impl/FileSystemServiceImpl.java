package com.xuecheng.file_system.service.impl;

import com.xuecheng.file_system.config.FastDFSProperties;
import com.xuecheng.file_system.dao.FileSystemRepository;
import com.xuecheng.file_system.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.ClientGlobal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理业务实现层
 *
 * @author Kang Yong
 * @date 2021/12/16
 * @since 1.0.0
 */
@Slf4j
@Service
public class FileSystemServiceImpl implements FileSystemService {

    @Autowired
    private FastDFSProperties fastDFSProperties;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    /**
     * 加载fdfs配置
     */
    private void initFdfsConfig() {
        try {
            ClientGlobal.initByProperties(fastDFSProperties.getTrackerServers());
            ClientGlobal.setG_connect_timeout(fastDFSProperties.getConnectTimeoutInSeconds());
            ClientGlobal.setG_network_timeout(fastDFSProperties.getNetworkTimeoutInSeconds());
            ClientGlobal.setG_charset(fastDFSProperties.getCharset());
        } catch (Exception e) {
            e.printStackTrace();
            // 初始化文件系统配置参数失败
            ExceptionCast.cast(FileSystemCode.FS_INITFDFS_ERROR);
        }
    }


    /**
     * 上传文件
     *
     * @param file        文件
     * @param filetag     文件标签
     * @param businesskey 业务key
     * @param metadata    元信息，json格式
     * @return
     */
    @Override
    public UploadFileResult upload(MultipartFile file, String filetag, String businesskey, String metadata) {
        return null;
    }

    /**
     * 上传文件到fdfs，返回文件id
     *
     * @param file 文件
     * @return
     */
    @Override
    public String fdfs_upload(MultipartFile file) {
        return null;
    }
}
