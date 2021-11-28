package com.xuecheng.manage.cms.config;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFSTemplateTest {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Test
    public void testStore() throws FileNotFoundException {
        File file = new File("D:\\code\\doit\\num02\\xcEduService01\\test-freemarker\\src\\main\\resources\\templates\\index_banner.ftl");
        FileInputStream fis = new FileInputStream(file);

        ObjectId objectId = gridFsTemplate.store(fis, "index_banner.ftl");
        System.out.println(objectId);
    }

    @Test
    public void queryFile() throws IOException {
        String fileId = "61a3795d65e5b01554d39397"; // 619f907665e5b00818c4e230
        // 根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        // 打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        // 获取流中的数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(
                content
        );

    }

}
