package com.xuecheng.manage_cms.service.impl;

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
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.CmsPageService;
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
     * ??????????????????
     *
     * @param page             ????????? ???1????????????
     * @param size
     * @param queryPageRequest ????????????
     * @return
     */
    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (ObjectUtils.isEmpty(queryPageRequest)) {
            // ?????????????????????????????????????????????
            queryPageRequest = new QueryPageRequest();
        }
        // ???????????????
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

        // ????????????
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size < 0) {
            size = 10;
        }
        // ????????????
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> cmsPagePage = cmsPageRepository.findAll(example, pageable);

        // ??????????????????
        QueryResult queryResult = new QueryResult();
        queryResult.setList(cmsPagePage.getContent()); // ??????
        queryResult.setTotal(cmsPagePage.getTotalElements()); // ?????????

        // ????????????
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    @Override
    public CmsPageResult add(CmsPage cmsPage) {
        // ???????????????????????????,??????????????????????????????path
        CmsPage existCmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

        // ?????????????????????
        if (!ObjectUtils.isEmpty(existCmsPage)) {
            // ?????????????????????
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

        // ??????
        cmsPage.setPageId(null);
        cmsPage.setPageCreateTime(new Date());
        cmsPageRepository.save(cmsPage);
        // ????????????
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
        // ???????????????????????????,??????????????????????????????path
        CmsPage cmsPageExists = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPageExists != null) {
            // ??????
            return this.edit(cmsPageExists.getPageId(), cmsPage);
        } else {
            // ??????
            return this.add(cmsPage);
        }
    }

    @Override
    public CmsPageResult edit(String pageId, CmsPage cmsPage) {
        // ?????????
        CmsPage existsCmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(existsCmsPage)) {
            // ???????????????????????????????????????
            return new CmsPageResult(CommonCode.FAIL, null);
        }

        // ????????????
        existsCmsPage.setTemplateId(cmsPage.getTemplateId());
        existsCmsPage.setSiteId(cmsPage.getSiteId());
        existsCmsPage.setPageAliase(cmsPage.getPageAliase());
        existsCmsPage.setPageName(cmsPage.getPageName());
        existsCmsPage.setPageWebPath(cmsPage.getPageWebPath());
        existsCmsPage.setPageParameter(cmsPage.getPageParameter());
        existsCmsPage.setDataUrl(cmsPage.getDataUrl());

        // ??????
        CmsPage savedCmsPage = cmsPageRepository.save(existsCmsPage);
        if (ObjectUtils.isEmpty(savedCmsPage)) {
            // ???????????????????????????????????????
//            return new CmsPageResult(CommonCode.FAIL, null);
//            throw new CustomerException(CommonCode.FAIL);
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return new CmsPageResult(CommonCode.SUCCESS, savedCmsPage);
    }

    @Override
    public ResponseResult delete(String pageId) {
        // ?????????
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
//            return new ResponseResult(CommonCode.FAIL);
            ExceptionCast.cast(CommonCode.FAIL);
        }

        // ??????
        cmsPageRepository.delete(cmsPage);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public String getPageHtml(String pageId) {
        // ????????????????????????
        Map model = this.getModelByPageId(pageId);
        if (ObjectUtils.isEmpty(model)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // ???????????? ??????
        String templateContent = this.getTemplateByPageId(pageId);
        if (StringUtils.isBlank(templateContent)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        // ???????????????
        String html = this.generateHtml(templateContent, model);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    @Override
    public ResponseResult postPage(String pageId) {
        // ?????????????????????
        String pageHtml = this.getPageHtml(pageId);
        if (StringUtils.isBlank(pageHtml)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        // ?????????????????????
        CmsPage cmsPage = this.savePage(pageId, pageHtml);

        // ??????rabbitmq??????
        this.sendPostPage(pageId);

        return ResponseResult.SUCCESS();
    }

    @Override
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        // 1????????????cms_page
        CmsPageResult cmsPageResult = this.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPage cmsPageSave = cmsPageResult.getCmsPage();

        // 2?????????????????????????????????????????????GridFS?????????mq?????????
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
     * ????????????id??????????????????
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
     * ????????????
     *
     * @param pageId
     */
    private void sendPostPage(String pageId) {
        // ????????????????????????
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // ????????????
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("pageId", pageId);
        String jsonString = JSON.toJSONString(msgMap);

        // ??????routingKey?????????????????????id
        String siteId = cmsPage.getSiteId();
        this.rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, jsonString);
    }

    /**
     * ?????????????????????
     *
     * @param pageId   ??????id
     * @param pageHtml ??????html??????
     * @return
     */
    private CmsPage savePage(String pageId, String pageHtml) {
        // ????????????
        Optional<CmsPage> cmsPageOptional = this.cmsPageRepository.findById(pageId);
        if (!cmsPageOptional.isPresent()) {
            // ???????????????
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // ??????????????????
        CmsPage cmsPage = cmsPageOptional.get();
        // ?????????????????????
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isNotBlank(htmlFileId)) {
            this.gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }

        // ??????html?????????gridFS
        InputStream inputStream = IOUtils.toInputStream(pageHtml);
        ObjectId objectId = this.gridFsTemplate.store(inputStream, cmsPage.getPageName());
        // ??????id
        String fileId = objectId.toString();
        // ?????????id?????????cmsPage???
        cmsPage.setHtmlFileId(fileId);
        this.cmsPageRepository.save(cmsPage);

        return cmsPage;
    }

    /**
     * freemarker ?????????????????????????????????????????????
     *
     * @param templateContent ??????
     * @param model           ??????
     * @return
     */
    private String generateHtml(String templateContent, Map model) {
        try {
            log.info("===???????????????{}", templateContent);
            // ???????????????
            Configuration configuration = new Configuration(Configuration.getVersion());
            // ???????????????
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", templateContent);
            // ?????????????????????
            configuration.setTemplateLoader(stringTemplateLoader);

            // ????????????
            Template template = configuration.getTemplate("template");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return html;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("======???????????????????????????");
        } catch (TemplateException e) {
            e.printStackTrace();
            log.error("======??????????????????HTML??????");
        }
        return null;
    }

    /**
     * ????????????id????????????????????????
     *
     * @param pageId
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        // ????????????
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // ??????????????????id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isBlank(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // ????????????
        Optional<CmsTemplate> cmsTemplateOptional = this.cmsTemplateRepository.findById(templateId);
        if (cmsTemplateOptional.isPresent()) {
            CmsTemplate cmsTemplate = cmsTemplateOptional.get();
            // ??????????????????????????????id
            String templateFileId = cmsTemplate.getTemplateFileId();
            if (StringUtils.isBlank(templateFileId)) {
                ExceptionCast.cast(CmsCode.CMS_TEMPLATEFIELD_ISNULL);
            }

            // ????????????????????????
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            // ?????????????????????
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // ??????GridFsResource
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
     * ????????????????????????
     *
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        // ??????id??????????????????
        CmsPage cmsPage = this.findById(pageId);
        if (ObjectUtils.isEmpty(cmsPage)) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // ??????dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isBlank(dataUrl)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // ??????dataUrl????????????
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl , Map.class);
        Map body = entity.getBody();
        return body;
    }
}
