package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理接口
 *
 * @author Kang Yong
 * @date 2021/12/16
 * @since 1.0.0
 */
@Api(value = "文件管理接口", description = "文件管理接口，提供数据模型的管理、查询")
public interface FileSystemControllerApi {

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @param filetag       文件标签
     * @param businesskey   业务key
     * @param metadata      元信息，json格式
     * @return
     */
    @ApiOperation(value = "上传文件")
    UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata);
}
