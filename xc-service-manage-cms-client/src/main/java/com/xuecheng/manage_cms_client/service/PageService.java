package com.xuecheng.manage_cms_client.service;

/**
 * @author Kang Yong
 * @date 2021/12/6
 * @since 1.0.0
 */
public interface PageService {

    /**
     * 将页面html保存到页面物理路径
     *
     * @param pageId 页面id
     */
    void savePageToServerPath(String pageId);
}
