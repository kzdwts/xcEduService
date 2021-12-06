package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * mongo持久层
 * 参数含义MongoRepository<CmsPage, String>
 * 第一个代表实体，第二个代表实体的主键类型
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {

}
