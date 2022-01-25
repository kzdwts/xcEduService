package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 字典
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SysDictionaryServiceTest extends TestCase {

    @Autowired
    private SysDictionaryService sysDictionaryService;

    @Test
    public void testFindDictionaryByType() {
        SysDictionary dictionaryByType = this.sysDictionaryService.findDictionaryByType("200");
        System.out.println(dictionaryByType.getDName());
    }
}