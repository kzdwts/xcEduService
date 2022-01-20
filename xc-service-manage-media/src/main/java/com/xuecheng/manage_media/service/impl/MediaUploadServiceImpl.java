package com.xuecheng.manage_media.service.impl;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import com.xuecheng.manage_media.service.MediaUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 媒资文件管理 业务实现层
 *
 * @author Kang Yong
 * @date 2022/1/19
 * @since 1.0.0
 */
@Slf4j
@Service
public class MediaUploadServiceImpl implements MediaUploadService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    // 上传文件保存路径
    @Value("${xc-service-manage-media.upload-location}")
    private String uploadPath;

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
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 检查文件是否上传
        // 1、得到文件的路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);

        // 2、查询数据库文件是否存在
        Optional<MediaFile> fileOptional = mediaFileRepository.findById(fileMd5);
        if (file.exists() && fileOptional.isPresent()) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        // 创建文件夹
        Boolean fileFold = this.createFileFold(fileMd5);
        if (!fileFold) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_CREATEFOLD_FAIL);
        }

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 创建文件目录
     *
     * @param fileMd5
     * @return
     */
    private Boolean createFileFold(String fileMd5) {
        String fileFolderPath = this.getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            // 创建文件夹
            boolean mkdirs = fileFolder.mkdirs();
            return mkdirs;
        }
        return true;
    }

    /**
     * 获取文件所在目录
     *
     * @param fileMd5
     * @return
     */
    private String getFileFolderPath(String fileMd5) {
        String fileFolderPath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        return fileFolderPath;
    }

    /**
     * 根据文件MD5得到文件路径
     * 规则：
     * 一级目录：MD5的第一个字符
     * 二级目录：MD5的第二个字符
     * 三极目录：MD5
     * 文件名：MD5 + 文件扩展名
     *
     * @param fileMd5 文件MD5
     * @param fileExt 文件扩展名
     * @return 文件路径
     */
    private String getFilePath(String fileMd5, String fileExt) {
        String filePath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
        return filePath;
    }

    /**
     * 获取文件目录相对路径，路径中去掉根目录
     *
     * @param fileMd5
     * @return
     */
    private String getFileFolderRelativePath(String fileMd5) {
        String filePath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        return filePath;
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
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        // 得到块文件所在路径
        String chunkfileFolderPath = this.getChunkFileFolderPath(fileMd5);

        // 块文件的文件名称以1,2,3... 序号命名，没有扩展名
        File chunkFile = new File(chunkfileFolderPath + chunk);
        if (chunkFile.exists()) {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, true);
        } else {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, false);
        }
    }

    /**
     * 得到块文件所在路径
     *
     * @param fileMd5
     * @return
     */
    private String getChunkFileFolderPath(String fileMd5) {
        String fileChunkFolderPath = this.getFileFolderPath(fileMd5) + "/" + "chunks" + "/";
        return fileChunkFolderPath;
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
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        // 上传文件不能为空
        if (null == file) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }

        // 创建块文件目录
        Boolean fileFolder = this.createChunkFileFolder(fileMd5);
        // 块文件
        File chunkFile = new File(this.getChunkFileFolderPath(fileMd5) + chunk);
        // 上传的块文件
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 创建块文件目录
     *
     * @param fileMd5
     * @return
     */
    private Boolean createChunkFileFolder(String fileMd5) {
        // 创建上传文件目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);

        if (!chunkFileFolder.exists()) {
            // 创建文件夹
            boolean mkdirs = chunkFileFolder.mkdirs();
            return mkdirs;
        }
        return true;
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
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 获取块文件的路径
        String chunkfileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkfileFolder = new File(chunkfileFolderPath);
        if (!chunkfileFolder.exists()) {
            chunkfileFolder.mkdir();
        }

        // 合并文件路径
        File mergeFile = new File(this.getFilePath(fileMd5, fileExt));
        // 创建合并文件
        // 合并文件存在先删除在创建
        if (mergeFile.exists()) {
            mergeFile.delete();
        }

        boolean newFile = false;
        try {
            newFile = mergeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("===合并文件块失败：{}", e.getMessage());
        }

        if (!newFile) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        // 获取块文件，此列表是已经排好序的列表
        List<File> chunkFiles = this.getChunkFiles(chunkfileFolder);
        // 合并文件
        mergeFile = this.mergeFile(mergeFile, chunkFiles);
        if (null == mergeFile) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }

        // 校验文件
        Boolean checkResult = this.checkFileMd5(mergeFile, fileMd5);
        if (!checkResult) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        // 将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        mediaFile.setFileOriginalName(fileName);
        // 文件路径保存相对路径
        mediaFile.setFilePath(this.getFileFolderRelativePath(fileMd5));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        // 状态为上传成功
        mediaFile.setFileStatus("301002");
        MediaFile save = mediaFileRepository.save(mediaFile);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 校验文件
     *
     * @param mergeFile
     * @param fileMd5
     * @return
     */
    private Boolean checkFileMd5(File mergeFile, String fileMd5) {
        // 非空校验
        if (null == mergeFile || StringUtils.isEmpty(fileMd5)) {
            return false;
        }

        // MD5校验
        FileInputStream mergeFileInputStream = null;
        try {
            mergeFileInputStream = new FileInputStream(mergeFile);
            // 得到文件的MD5
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileInputStream);
            // 比较md5
            if (fileMd5.equalsIgnoreCase(mergeFileMd5)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("checkFileMd5error,fileis:{},md5is:{}", mergeFile.getAbsoluteFile(), fileMd5);
        } finally {
            try {
                mergeFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 合并块文件
     *
     * @param mergeFile
     * @param chunkFiles
     * @return
     */
    private File mergeFile(File mergeFile, List<File> chunkFiles) {
        // 合并文件
        try {
            // 写文件对象
            RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw");
            // 遍历分块文件开始合并
            // 读取文件缓冲区
            byte[] buffer = new byte[1024];
            for (File chunkFile : chunkFiles) {
                // 读文件对象
                RandomAccessFile rafRead = new RandomAccessFile(chunkFile, "r");
                int len = -1;
                // 读取分块文件
                while ((len = rafRead.read(buffer)) != -1) {
                    // 向合并文件中国写数据
                    rafWrite.write(buffer, 0, len);
                }
                rafRead.close();
            }
            rafWrite.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("mergefileerror:{}", e.getMessage());
            return null;
        }
        
        return mergeFile;
    }

    /**
     * 获取文件夹下所有块文件（排好序的）
     *
     * @param chunkfileFolder
     * @return
     */
    private List<File> getChunkFiles(File chunkfileFolder) {
        // 获取路径下所有块文件
        File[] chunkFiles = chunkfileFolder.listFiles();
        // 将文件数组转为list，并排序
        List<File> chunkFileList = new ArrayList<>(chunkFiles.length);
        chunkFileList.addAll(Arrays.asList(chunkFiles));

        // 排序
        Collections.sort(chunkFileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });
        return chunkFileList;
    }
}
