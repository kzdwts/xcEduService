package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * mongo持久层
 * 参数含义MongoRepository<SysDictionary, String>
 * 第一个代表实体，第二个代表实体的主键类型
 */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary, String> {

    /**
     * 根据字典分类查询字典信息
     *
     * @param type
     * @return
     */
    SysDictionary findBydType(String type);
}
