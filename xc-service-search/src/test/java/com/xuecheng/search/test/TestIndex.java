package com.xuecheng.search.test;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
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

    /**
     * 测试创建索引
     *
     * @author Kang Yong
     * @date 2021/12/31
     */
    @Test
    public void testCreateIndex() throws IOException {
        // 创建索引对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        // 设置参数
        createIndexRequest.settings(
                Settings.builder().put("number_of_shards", 1).put("number_of_replicas", 0));
        // 指定映射
        createIndexRequest.mapping("doc", "{\n" +
                "    \"properties\": {\n" +
                "        \"name\": {\n" +
                "            \"type\": \"text\"\n" +
                "        },\n" +
                "        \"description\": {\n" +
                "            \"type\": \"text\"\n" +
                "        },\n" +
                "        \"studymodel\": {\n" +
                "            \"type\": \"keyword\"\n" +
                "        }\n" +
                "    }\n" +
                "}", XContentType.JSON);

        // 操作索引的客户端
        IndicesClient indices = client.indices();
        // 执行创建操作
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }


}
