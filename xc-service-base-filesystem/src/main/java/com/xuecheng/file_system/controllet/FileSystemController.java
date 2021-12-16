package com.xuecheng.file_system.controllet;

import com.xuecheng.api.filesystem.FileSystemControllerApi;
import com.xuecheng.file_system.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理
 *
 * @author Kang Yong
 * @date 2021/12/16
 * @since 1.0.0
 */
@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {

    @Autowired
    private FileSystemService fileSystemService;

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
    @PostMapping("/upload")
    public UploadFileResult upload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "filetag", required = false) String filetag,
                                   @RequestParam(value = "businesskey", required = false) String businesskey,
                                   @RequestParam(value = "metadata", required = false) String metadata) {
        return fileSystemService.upload(file, filetag, businesskey, metadata);
    }
}
