package com.xuecheng.search.service.impl;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 课程搜索
 *
 * @author Kang Yong
 * @date 2022/1/6
 * @since 1.0.0
 */
@Slf4j
@Service
public class EsCourseServiceImpl implements EsCourseService {

    @Value("${xuecheng.course.index}")
    private String index;

    @Value("${xuecheng.course.type}")
    private String type;

    @Value("${xuecheng.course.source-field}")
    private String sourceField;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 课程搜索
     *
     * @param page              第几页
     * @param size              每页多少条数据
     * @param courseSearchParam 搜索条件
     * @return
     */
    @Override
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        // 参数
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }

        // 创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        // 设置搜索类型
        searchRequest.types(type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 过滤原字段
        String[] sourceFieldArr = sourceField.split(",");
        searchSourceBuilder.fetchSource(sourceFieldArr, new String[]{});

        // boolean查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 搜索条件
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                    .multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("70%")
                    .field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            // 根据一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            // 根据二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            // 根据难度
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }


        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        QueryResult<CoursePub> queryResult = new QueryResult();
        // 执行搜索
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            // 响应结果
            SearchHits hits = searchResponse.getHits();
            // 得到的总记录数
            long totalHits = hits.totalHits;

            // 数据列表
            List<CoursePub> list = new ArrayList<>();
            // 取出内容
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                CoursePub coursePub = new CoursePub();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                // 取出 name
                String name = (String) sourceAsMap.get("name");
                coursePub.setName(name);
                // 取出 图片
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                // 取出价格
                Double price = null;
                Double priceOld = null;
                try {
                    if (!ObjectUtils.isEmpty(sourceAsMap.get("price"))) {
                        price = Double.parseDouble((String) sourceAsMap.get("price"));
                    }
                    coursePub.setPrice(price);
                    if (!ObjectUtils.isEmpty(sourceAsMap.get("price_old"))) {
                        priceOld = Double.parseDouble((String) sourceAsMap.get("price_old"));
                    }
                    coursePub.setPrice_old(priceOld);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                list.add(coursePub);
            }

            // 总数量
            queryResult.setTotal(totalHits);
            queryResult.setList(list);
            QueryResponseResult<CoursePub> coursePubQueryResponseResult = new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);

            return coursePubQueryResponseResult;
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
