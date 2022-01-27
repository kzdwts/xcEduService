package com.xuecheng.search.service.impl;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
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
import java.util.HashMap;
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

    @Value("${xuecheng.media.index}")
    private String mediaIndex;

    @Value("${xuecheng.media.type}")
    private String mediaType;

    @Value("${xuecheng.media.source-field}")
    private String mediaSourceField;

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

        // 分页
        if (page <= 0) {
            page = 1;
        }
        int from = (page - 1) * size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

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

    /**
     * 根据课程id查询课程信息
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, CoursePub> getall(String id) {
        // 设置索引库
        SearchRequest searchRequest = new SearchRequest(index);
        // 设置类型
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 查询条件，根据课程id查询
        searchSourceBuilder.query(QueryBuilders.termsQuery("id", id));
        // 取消source原字段过滤，查询所有字段
        searchRequest.source(searchSourceBuilder);

        // 执行搜索
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取搜索结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        Map<String, CoursePub> map = new HashMap<>();
        for (SearchHit hit : searchHits) {
            String courseId = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String id1 = (String) sourceAsMap.get("id");
            String name = (String) sourceAsMap.get("name");
            String grade = (String) sourceAsMap.get("grade");
            String charge = (String) sourceAsMap.get("charge");
            String pic = (String) sourceAsMap.get("pic");
            String description = (String) sourceAsMap.get("description");
            String teachplan = (String) sourceAsMap.get("teachplan");

            CoursePub coursePub = new CoursePub();
            coursePub.setId(courseId);
            coursePub.setName(name);
            coursePub.setPic(pic);
            coursePub.setGrade(grade);
            coursePub.setTeachplan(teachplan);
            coursePub.setDescription(description);
            map.put(courseId, coursePub);
        }

        return map;
    }

    /**
     * 根据课程计划查询媒资信息
     *
     * @param teachplanIds {@link String[]}
     * @return {@link QueryResponseResult<  TeachplanMediaPub >}
     * @author Kang Yong
     * @date 2022/1/27
     */
    @Override
    public QueryResponseResult<TeachplanMediaPub> getmedia(String[] teachplanIds) {
        // 设置索引
        SearchRequest searchRequest = new SearchRequest(mediaIndex);
        // 设置类型
        searchRequest.types(mediaType);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // source源字段过滤
        String[] sourceFields = mediaSourceField.split(",");
        searchSourceBuilder.fetchSource(sourceFields, new String[]{});
        // 查询条件，根据课程计划id查询，可传入多个id
        searchSourceBuilder.query(QueryBuilders.termQuery("teachplan_id", teachplanIds));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        // 执行搜索
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("===调用es查询异常");
        }

        // 获取搜索结果
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();

        // 数据列表
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            // 取出课程计划媒资信息
            String courseid = (String) sourceAsMap.get("courseid");
            String media_id = (String) sourceAsMap.get("media_id");
            String media_url = (String) sourceAsMap.get("media_url");
            String teachplan_id = (String) sourceAsMap.get("teachplan_id");
            String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");

            teachplanMediaPub.setCourseId(courseid);
            teachplanMediaPub.setMediaUrl(media_url);
            teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
            teachplanMediaPub.setMediaId(media_id);
            teachplanMediaPub.setTeachplanId(teachplan_id);
            // 将数据加入列表
            teachplanMediaPubList.add(teachplanMediaPub);
        }

        // 构建返回课程媒资信息对象
        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setList(teachplanMediaPubList);
        QueryResponseResult<TeachplanMediaPub> queryResponseResult = new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
}
