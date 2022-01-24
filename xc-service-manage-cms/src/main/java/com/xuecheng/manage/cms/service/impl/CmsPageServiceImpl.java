package com.xuecheng.manage.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage.cms.config.RabbitmqConfig;
import com.xuecheng.manage.cms.dao.CmsPageRepository;
import com.xuecheng.manage.cms.dao.CmsSiteRepository;
import com.xuecheng.manage.cms.dao.CmsTemplateRepository;
import com.xuecheng.manage.cms.service.CmsPageService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CmsPageServiceImpl implements CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 页面查询方法
     *
     * @param page             页码， 从1开始计数
     * @param size
     * @param queryPageRequest 查询条件
     * @return
     */
    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (ObjectUtils.isEmpty(queryPageRequest)) {
            // 保证后边的逻辑不会包空指针异常
            queryPageRequest = new QueryPageRequest();
        }
        // 自定义条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        // 参数处理
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size < 0) {
            size = 10;
        }
        // 分页参数
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> cmsPagePage = cmsPageRepository.findAll(example, pageable);

        // 封装返回结果
        QueryResult queryResult = new QueryResult();
        queryResult.setList(cmsPagePage.getContent()); // 内容
        queryResult.setTotal(cmsPagePage.getTotalElements()); // 总数量

        // 返回结果
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    @Override
    public CmsPageResult add(CmsPage cmsPage) {
        // 先查询是否已经有了,页面名称、站点、页面path
        CmsPage existCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        // 如果没有就新增
        if (!ObjectUtils.isEmpty(existCmsPage)) {
            // 页面名称已存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

        // 新增
        cmsPage.setPageId(null);
        cmsPage.setPageCreateTime(new Date());
        cmsPageRepository.save(cmsPage);
        // 返回成功
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    @Override
    public CmsPage findById(String pageId) {
        Optional<CmsPage> opCmsPage = cmsPageRepository.findById(pageId);
        if (opCmsPage.isPresent()) {
            CmsPage cmsPage = opCmsPage.get();
            return cmsPage;
        }
        return null;
    }

    @Override
    public CmsPageResult save(CmsPage cmsPage) {
        // 先查询是否已经有了,页面名称、站点、页面path
        CmsPage cmsPageExists = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPageExists != null) {
            // 更新
            return this.edit(cmsPageExists.getPageId(), cmsPage);
        } else {
            // 添加
            return this.add(cmsPage);
        }
    }

    @Override
    public CmsPageResult edit(String pageId, CmsPage cmsPage) {
        // 先查询
        CmsPage existsCmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(existsCmsPage)) {
            // 如果为空，直接返回错误信息
            return new CmsPageResult(CommonCode.FAIL, null);
        }

        // 补全参数
        existsCmsPage.setTemplateId(cmsPage.getTemplateId());
        existsCmsPage.setSiteId(cmsPage.getSiteId());
        existsCmsPage.setPageAliase(cmsPage.getPageAliase());
        existsCmsPage.setPageName(cmsPage.getPageName());
        existsCmsPage.setPageWebPath(cmsPage.getPageWebPath());
        existsCmsPage.setPageParameter(cmsPage.getPageParameter());
        existsCmsPage.setDataUrl(cmsPage.getDataUrl());

        // 更新
        CmsPage savedCmsPage = cmsPageRepository.save(existsCmsPage);
        if (ObjectUtils.isEmpty(savedCmsPage)) {
            // 如果为空，直接返回错误信息
//            return new CmsPageResult(CommonCode.FAIL, null);
//            throw new CustomerException(CommonCode.FAIL);
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return new CmsPageResult(CommonCode.SUCCESS, savedCmsPage);
    }

    @Override
    public ResponseResult delete(String pageId) {
        // 先查询
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
//            return new ResponseResult(CommonCode.FAIL);
            ExceptionCast.cast(CommonCode.FAIL);
        }

        // 删除
        cmsPageRepository.delete(cmsPage);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public String getPageHtml(String pageId) {
        // 获取页面模型数据
        Map model = this.getModelByPageId(pageId);
        if (ObjectUtils.isEmpty(model)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // 获取页面 模板
        String templateContent = this.getTemplateByPageId(pageId);
        if (StringUtils.isBlank(templateContent)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        // 页面静态化
        String html = this.generateHtml(templateContent, model);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    @Override
    public ResponseResult postPage(String pageId) {
        // 执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        if (StringUtils.isBlank(pageHtml)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        // 保存静态化文件
        CmsPage cmsPage = this.savePage(pageId, pageHtml);

        // 发送rabbitmq通知
        this.sendPostPage(pageId);

        return ResponseResult.SUCCESS();
    }

    @Override
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        // 1、保存到cms_page
        CmsPageResult cmsPageResult = this.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPage cmsPageSave = cmsPageResult.getCmsPage();

        // 2、执行页面发布（先静态化，保存GridFS，发送mq消息）
        ResponseResult responseResult = this.postPage(cmsPageSave.getPageId());
        if (!responseResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String siteId = cmsPageSave.getSiteId();
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        String pageUrl = cmsSite.getSiteDomain() + cmsSite.getSiteWebPath() + cmsSite.getSiteWebPath() + cmsPageSave.getPageName();

        return new CmsPostPageResult(CommonCode.SUCCESS, pageUrl);
    }

    /**
     * 根据站点id查询站点信息
     *
     * @param siteId
     * @return
     */
    private CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optionalCmsSite = this.cmsSiteRepository.findById(siteId);
        if (optionalCmsSite.isPresent()) {
            return optionalCmsSite.get();
        }
        return null;
    }

    /**
     * 发送消息
     *
     * @param pageId
     */
    private void sendPostPage(String pageId) {
        // 查询页面是否存在
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // 组装参数
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("pageId", pageId);
        String jsonString = JSON.toJSONString(msgMap);

        // 路由routingKey，使用的是站点id
        String siteId = cmsPage.getSiteId();
        this.rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, jsonString);
    }

    /**
     * 保存静态化页面
     *
     * @param pageId   页面id
     * @param pageHtml 页面html内容
     * @return
     */
    private CmsPage savePage(String pageId, String pageHtml) {
        // 查询页面
        Optional<CmsPage> cmsPageOptional = this.cmsPageRepository.findById(pageId);
        if (!cmsPageOptional.isPresent()) {
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // 获取页面数据
        CmsPage cmsPage = cmsPageOptional.get();
        // 存储之前先删除
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isNotBlank(htmlFileId)) {
            this.gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }

        // 保存html文件到gridFS
        InputStream inputStream = IOUtils.toInputStream(pageHtml);
        ObjectId objectId = this.gridFsTemplate.store(inputStream, cmsPage.getPageName());
        // 文件id
        String fileId = objectId.toString();
        // 将文件id存储到cmsPage中
        cmsPage.setHtmlFileId(fileId);
        this.cmsPageRepository.save(cmsPage);

        return cmsPage;
    }

    /**
     * freemarker 根据页面模板和数据生成静态页面
     *
     * @param templateContent 模板
     * @param model           数据
     * @return
     */
    private String generateHtml(String templateContent, Map model) {
        try {
            // 生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            // 模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", templateContent);
            // 配置模板加载器
            configuration.setTemplateLoader(stringTemplateLoader);
            log.info("===模板信息：{}", templateContent);

            // 获取模板
            Template template = configuration.getTemplate("template");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return html;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("======配置类获取模板异常");
        } catch (TemplateException e) {
            e.printStackTrace();
            log.error("======根据模板生成HTML异常");
        }
        return null;
    }

    /**
     * 根据页面id获取页面模型信息
     *
     * @param pageId
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        // 查询页面
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 获取页面模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isBlank(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 查询模板
        Optional<CmsTemplate> cmsTemplateOptional = this.cmsTemplateRepository.findById(templateId);
        if (cmsTemplateOptional.isPresent()) {
            CmsTemplate cmsTemplate = cmsTemplateOptional.get();
            // 获取模板文件存储位置id
            String templateFileId = cmsTemplate.getTemplateFileId();
            if (StringUtils.isBlank(templateFileId)) {
                ExceptionCast.cast(CmsCode.CMS_TEMPLATEFIELD_ISNULL);
            }

            // 取出模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            // 打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // 创建GridFsResource
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取页面模型数据
     *
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        // 根据id查询页面数据
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // 获取dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isBlank(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // 根据dataUrl获取数据
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl , Map.class);
        Map body = entity.getBody();
        return body;
    }
}
