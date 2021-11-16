package com.xuecheng.manage.cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * mongo cms-template持久层
 * 参数含义MongoRepository<CmsTemplate, String>
 * 第一个代表实体，第二个代表实体的主键类型
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {
}
