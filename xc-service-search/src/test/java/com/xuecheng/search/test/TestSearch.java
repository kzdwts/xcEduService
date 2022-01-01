package com.xuecheng.search.test;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void testSearchAll() {

    }
}
