package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * mongo cms-site持久层
 * 参数含义MongoRepository<CmsSite, String>
 * 第一个代表实体，第二个代表实体的主键类型
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {
}
