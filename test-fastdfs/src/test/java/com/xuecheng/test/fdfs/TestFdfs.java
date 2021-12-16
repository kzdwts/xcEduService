package com.xuecheng.test.fdfs;

import com.xuecheng.test.fdfs.config.FastDFSProperties;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件上传下载测试类
 *
 * @author Kang Yong
 * @date 2021/12/14
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFdfs {

    @Autowired
    private FastDFSProperties dfsProperties;

    /**
     * 上传文件测试
     */
    @Test
    public void testUpload() throws IOException, MyException {
        // 测试读取配置文件
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        System.out.println(ClientGlobal.g_network_timeout);
        System.out.println(ClientGlobal.g_charset);

        // 创建客户端
        TrackerClient tc = new TrackerClient();
        // 连接trackerserver
        TrackerServer ts = tc.getTrackerServer();
        if (ts == null) {
            System.out.println("ts=null");
            return;
        }

        // 获取一个storage server
        StorageServer ss = tc.getStoreStorage(ts);
        if (ss == null) {
            System.out.println("ss=null");
            return;
        }

        // 创建一个存储客户端
        StorageClient1 sc1 = new StorageClient1(ts, ss);
        NameValuePair[] meta_list = null;
        String item = "C:\\Users\\Administrator\\Pictures\\pic\\b002.jpg";
        String fileid = sc1.upload_file1(item, "jpg", meta_list);
        System.out.println("item = " + item);
        System.out.println("fileid = " + fileid);
//        item = C:\Users\Administrator\Pictures\pic\b002.jpg
//                fileid = group1/M00/00/00/wKhkhGG7RKOAf-i0AAPNnHO5jqo048.jpg
    }

    /**
     * 查询文件
     *
     * @throws MyException
     * @throws IOException
     */
    @Test
    public void testQueryFile() throws MyException, IOException {
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        StorageServer storageServer = null;

        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        FileInfo fileInfo = storageClient.query_file_info("group1", "M00/00/00/wKhkhGG7RKOAf-i0AAPNnHO5jqo048.jpg");
        System.out.println(fileInfo);
//        fetch_from_server = true, file_type = 1, source_ip_addr = 192.168.100.132, file_size = 249244, create_timestamp = 2021-12-16 21:52:35, crc32 = 1941540522
    }

    /**
     * 测试下载文件
     */
    @Test
    public void testDownloadFile() throws IOException, MyException {
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        StorageServer storageServer = null;
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
        byte[] bytes = storageClient1.download_file1("group1/M00/00/00/wKhkhGG7RKOAf-i0AAPNnHO5jqo048.jpg");

        File file = new File("D://1.jpg");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();
        System.out.println("Game Over!");
    }

}
