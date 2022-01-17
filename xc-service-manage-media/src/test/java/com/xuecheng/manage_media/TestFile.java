package com.xuecheng.manage_media;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * 文件测试
 *
 * @author Kang Yong
 * @date 2022/1/16
 * @since 1.0.0
 */
public class TestFile {

    /**
     * 测试文件分块
     */
    @Test
    public void chunkFile() throws IOException {
        // 原文件
        File originFile = new File("D:\\data\\video\\lucene.avi");
        // 分块文件目录
        String chunkPath = "D:\\data\\video\\chunk\\";
        File chunkFileFolder = new File(chunkPath);

        // 每块大小
        long chunkSize = 1 * 1024 * 1024;
        // 分多少块
        long chunkNums = (long) Math.ceil(originFile.length() * 1.0 / chunkSize);

        // 读文件
        RandomAccessFile rafRead = new RandomAccessFile(originFile, "r");
        // 分块，生成文件
        byte[] buffer = new byte[1024];
        // 分块
        for (int i = 0; i < chunkNums; i++) {
            // 创建新文件
            File file = new File(chunkPath + i);
            boolean newFile = file.createNewFile();

            RandomAccessFile rafaWrite = new RandomAccessFile(file, "rw");
            if (newFile) {
                int len = -1;
                while ((len = rafRead.read(buffer)) != -1) {
                    rafaWrite.write(buffer, 0, len);
                    if (file.length() >= chunkSize) {
                        break;
                    }
                }
                rafaWrite.close();
            }
        }
        rafRead.close();

    }

    /**
     * 测试合并文件
     */
    @Test
    public void testMerge() throws IOException {
        // 文件夹路径
        String chunkFileFolderPath = "D:\\data\\video\\chunk\\";
        File chunkFileFolder = new File(chunkFileFolderPath);
        // 文件排序
        File[] files = chunkFileFolder.listFiles();
        List<File> fileList = new ArrayList<>(Arrays.asList(files));
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    // 升序
                    return 1;
                }
                return -1;
            }
        });

        // 目标文件
        String mergeFilePath = "D:\\data\\video\\lucene_merge.avi";
        File mergeFile = new File(mergeFilePath);
        boolean newFile = mergeFile.createNewFile();

        // 写文件
        RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw");
        byte[] buffer = new byte[1024];
        for (File file : fileList) {
            int len = -1;
            RandomAccessFile rafRead = new RandomAccessFile(file, "r");
            while ((len = rafRead.read(buffer)) != -1) {
                rafWrite.write(buffer, 0, len);
            }

            rafRead.close();
        }
        rafWrite.close();
    }

}
