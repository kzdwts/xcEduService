package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * mongo持久层
 * 参数含义MongoRepository<CmsPage, String>
 * 第一个代表实体，第二个代表实体的主键类型
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {

    /**
     * 自定义方法，根据页面名称查询
     *
     * @param pageName 页面名称
     * @return 页面信息
     */
    CmsPage findByPageName(String pageName);

    /**
     * 根据页面名称，站点id，页面路径查询页面是否存在
     *
     * @param pageName    页面名称
     * @param siteId      站点id
     * @param pageWebPath 页面路径
     * @return
     */
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);
}
