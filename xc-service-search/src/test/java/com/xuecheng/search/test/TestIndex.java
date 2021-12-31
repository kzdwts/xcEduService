package com.xuecheng.search.test;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 测试添加文档
     *
     * @author Kang Yong
     * @date 2021/12/31
     */
    @Test
    public void testAddDoc() throws IOException {
        // 创建文档
        // 准备参数
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "Bootstrap开发框架");
        jsonMap.put("description", "Bootstrap是由Twitter推出的一个前台页面开发框架，在行业之中使用较为广泛。此开发框架包含了大量的CSS、JS程序代码，可以帮助开发者（尤其是不擅长页面开发的程序人员）轻松的实现一个不受浏览器限制的精美界面效果。");
        jsonMap.put("studymodel", "201001");
        jsonMap.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        jsonMap.put("price", 5.6f);

        // 索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
        // 指定索引内容
        indexRequest.source(jsonMap);

        // 执行
        IndexResponse indexResponse = client.index(indexRequest);
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }

    /**
     * 测试查询文档
     *
     * @author Kang Yong
     * @date 2021/12/31
     */
    @Test
    public void testGetDoc() throws IOException {
        // 查询对象
        GetRequest getRequest = new GetRequest("xc_course", "doc", "文档id");
        GetResponse getResponse = client.get(getRequest);
        boolean exists = getResponse.isExists();
        if (exists) {
            Map<String, Object> sourceMap = getResponse.getSource();
            System.out.println(sourceMap);
        }
    }

    /**
     * 更新文档
     *
     * @author Kang Yong
     * @date 2021/12/31
     */
    @Test
    public void testUpdateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("xc_course", "doc", "文档id");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "Bootstrap开发框架");
        jsonMap.put("description", "Bootstrap是由Twitter推出的一个前台页面开发框架，在行业之中使用较为广泛。此开发框架包含了大量的CSS、JS程序代码，可以帮助开发者（尤其是不擅长页面开发的程序人员）轻松的实现一个不受浏览器限制的精美界面效果。");
        jsonMap.put("studymodel", "201001");
        jsonMap.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        jsonMap.put("price", 5.6f);
        updateRequest.doc(jsonMap);

        // 更新
        UpdateResponse update = client.update(updateRequest);
        RestStatus status = update.status();
        System.out.println(status);
    }

    /**
     * 测试删除文档
     *
     * @author Kang Yong
     * @date 2021/12/31
     */
    @Test
    public void testDeleteDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("xc_course", "doc", "文档id");
        DeleteResponse deleteResponse = client.delete(deleteRequest);
        DocWriteResponse.Result result = deleteResponse.getResult();
        System.out.println(result);
    }


}
