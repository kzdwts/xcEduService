package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsTeachplanMediaPubControllerApi;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程计划媒资信息
 *
 * @author Kang Yong
 * @date 2022/1/27
 * @since 1.0.0
 */
@RestController
@RequestMapping("/search/media")
public class EsTeachplanMediaPubController implements EsTeachplanMediaPubControllerApi {

    @Autowired
    private EsCourseService esCourseService;

    @Override
    public TeachplanMediaPub getmedia(String teachplanId) {
        String[] teachplanIds = new String[]{teachplanId};
        QueryResponseResult<TeachplanMediaPub> mediaPubQueryResponseResult = this.esCourseService.getmedia(teachplanIds);
        QueryResult<TeachplanMediaPub> queryResult = mediaPubQueryResponseResult.getQueryResult();
        if (queryResult != null
                && queryResult.getList() != null
                && queryResult.getList().size() > 0
        ) {
            return queryResult.getList().get(0);
        }
        return new TeachplanMediaPub();
    }
}
