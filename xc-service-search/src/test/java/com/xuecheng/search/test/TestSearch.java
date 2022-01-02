package com.xuecheng.search.test;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * 查询
 *
 * @author Kang Yong
 * @date 2022/1/1
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private RestClient restClient;

    /**
     * 搜索全部记录
     *
     * @author Kang Yong
     * @date 2022/1/1
     */
    @Test
    public void testSearchAll() throws IOException {
        // 搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置搜索类型
        searchRequest.searchType("doc");
        // 指定源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置源字段过滤,第一个参数，结果集包含哪些字段，第二个参数：结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});
        // 搜索方式
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索结果
        SearchHits hits = searchResponse.getHits();
        // 匹配到的总记录数
        long totalHits = hits.getTotalHits();
        // 得到匹配度高的文档
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit : hits1) {
            // 文档的id
            String id = hit.getId();
            // 源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");

        }


    }

    /**
     * 测试分页
     */
    @Test
    public void testSearchPage() throws IOException {
        // 搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置搜索类型
        searchRequest.searchType("doc");
        // 指定源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置源字段过滤,第一个参数，结果集包含哪些字段，第二个参数：结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{});
        // 搜索方式
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        // 分页
        int page = 1;
        int size = 1;
        int from = (page - 1) / size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索结果
        SearchHits hits = searchResponse.getHits();
        // 匹配到的总记录数
        long totalHits = hits.getTotalHits();
        // 得到匹配度高的文档
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit : hits1) {
            // 文档的id
            String id = hit.getId();
            // 源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");

        }
    }

}
