package com.xuecheng.file_system.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.file_system.config.FastDFSProperties;
import com.xuecheng.file_system.dao.FileSystemRepository;
import com.xuecheng.file_system.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
            log.info("===fdfsp配置参数：{}", fastDFSProperties);
//            ClientGlobal.initByProperties(fastDFSProperties.getTrackerServers());
            ClientGlobal.initByTrackers(fastDFSProperties.getTrackerServers());
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
        // 文件不能为空
        if (file == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }

        // 上传
        String fileId = this.fdfs_upload(file);
        // 创建文件信息对象
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        // 元数据
        if (StringUtils.isNotBlank(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystem.setFileName(file.getOriginalFilename());
        fileSystem.setFileSize(file.getSize());
        fileSystem.setFileType(file.getContentType());
        this.fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    /**
     * 上传文件到fdfs，返回文件id
     *
     * @param file 文件
     * @return
     */
    @Override
    public String fdfs_upload(MultipartFile file) {
        try {
            // 加载配置
            this.initFdfsConfig();
            // 创建tracker client
            TrackerClient trackerClient = new TrackerClient();
            // 获取trackerServer
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            // 获取Storage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String file1 = storageClient1.upload_file1(bytes, extName, null);
            return file1;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件错误：{}", e.getMessage());
        }

        return null;
    }
}
