package com.xuecheng.search.test;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * es索引测试类
 *
 * @author Kang Yong
 * @date 2021/12/31
 * @since 1.0.0
 */
public class TestIndex {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private RestClient restClient;

    /**
     * 测试删除索引
     *
     * @author Kang Yong
     * @date 2021/12/31
     */
    @Test
    public void testDeleteIndex() throws IOException {
        // 删除索引对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        // 操作索引的客户端
        IndicesClient indices = client.indices();
        // 执行删除操作
        DeleteIndexResponse delete = indices.delete(deleteIndexRequest);
        // 获取删除结果
        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);

    }
}
