package com.xuecheng.manage_cms_client.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * 页面业务实现层
 *
 * @author Kang Yong
 * @date 2021/12/6
 * @since 1.0.0
 */
@Slf4j
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Override
    public void savePageToServerPath(String pageId) {
        // 获取页面信息
        CmsPage cmsPage = this.findCmsPageByPageId(pageId);
        if (cmsPage == null) {
            log.error("未查询到页面信息, pageId:{}", pageId);
            return;
        }

        // 站点id
        String siteId = cmsPage.getSiteId();
        // 获取站点信息
        CmsSite cmsSite = this.findCmsSiteBySiteId(siteId);
        if (cmsSite == null) {
            log.error("未查询到站点信息, siteId:{}", siteId);
            return;
        }

        // 页面物理路径
        String pagePath = cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        // 查询页面静态文件
        String htmlFileId = cmsPage.getHtmlFileId();
        // 获取文件流
        InputStream inputStream = this.getFileById(htmlFileId);
        if (inputStream == null) {
            log.error("未获取到文件信息, htmlFileId:{}", htmlFileId);
            return;
        }

        // 生成文件
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据文件id，从mongodb查询文件，获取文件流
     *
     * @param htmlFileId
     * @return
     */
    private InputStream getFileById(String htmlFileId) {
        // 查询文件存储位置
        GridFSFile gridFSFile = this.gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        // 打开文件流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 资源对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        // 返回文件流
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据站点id查询站点信息
     *
     * @param siteId
     * @return
     */
    private CmsSite findCmsSiteBySiteId(String siteId) {
        Optional<CmsSite> optional = this.cmsSiteRepository.findById(siteId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 根据页面id查询页面信息
     *
     * @param pageId
     * @return
     */
    private CmsPage findCmsPageByPageId(String pageId) {
        // 根据页面id查询页面信息
        Optional<CmsPage> optional = this.cmsPageRepository.findById(pageId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

}
