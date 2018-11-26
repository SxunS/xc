package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository pageRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(PageService.class);
    /**
     * 页面列表查询
     * @param pageNo 第几页
     * @param pageSize 每页条数
     * @param pageRequest 请求参数
     * @return 分页对象queryResponseResult
     */
    public QueryResponseResult findList(int pageNo, int pageSize, QueryPageRequest pageRequest){
        if (pageRequest == null) {
            pageRequest = new QueryPageRequest();
        }
        pageNo--;
        if (pageNo < 0) {
            pageNo = 0;
        }
        if (pageSize < 1) {
            pageSize = 30;
        }
        //设置条件值
        CmsPage cmsPage = new CmsPage();
        //站点id
        String siteId = pageRequest.getSiteId();
        if (StringUtils.isNoneBlank(siteId)) {
            cmsPage.setSiteId(siteId);
        }
        //页面别名
        String pageAliase = pageRequest.getPageAliase();
        if (StringUtils.isNoneBlank(pageAliase)) {
            cmsPage.setPageAliase(pageAliase);
        }
        //条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //创建条件查询实例
        Example<CmsPage> example = Example.of(cmsPage, matcher);
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<CmsPage> all = pageRepository.findAll(example,pageable);
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<>();
        cmsPageQueryResult.setTotal(all.getTotalPages());
        cmsPageQueryResult.setList(all.getContent());
        return new QueryResponseResult(CommonCode.SUCCESS,cmsPageQueryResult);
    }

    /**
     * 添加页面
     * @param cmsPage 页面对象
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage){

        //根据唯一性校验，页面是否存在
        CmsPage page = pageRepository.findByPageNameAndSiteIdAndPageWebPath(
                cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath()
        );
        if (page == null) {
            cmsPage.setPageId(null);
            CmsPage save = pageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,save);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 删除页面
     * @param id 页面id
     * @return springData ResponseResult
     */
    public ResponseResult del(String id){
        CmsPage page = findById(id);
        if (page != null) {
            pageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
    /**
     * 根据id 查找
     * @param id 页面id
     * @return cmsPage
     */
    public CmsPage findById(String id){
        Optional<CmsPage> optional = pageRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 根据id 更新cmsPage页面
     * @param id cmsPage id
     * @param cmsPage 更新后的页面
     * @return cmsPageResult
     */
    public CmsPageResult edit(String id, CmsPage cmsPage){
        CmsPage oldCmsPage = findById(id);
        if (oldCmsPage != null) {
            oldCmsPage.setTemplateId(cmsPage.getTemplateId());
            oldCmsPage.setSiteId(cmsPage.getSiteId());
            oldCmsPage.setPageAliase(cmsPage.getPageAliase());
            oldCmsPage.setPageName(cmsPage.getPageName());
            oldCmsPage.setPageWebPath(cmsPage.getPageWebPath());
            oldCmsPage.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            oldCmsPage.setDataUrl(cmsPage.getDataUrl());
            CmsPage save = pageRepository.save(oldCmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,save);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 页面静态化
     */
    public String getHtml(String pageId) {
        //获取页面模型
        String model = getModel(pageId);
        if (StringUtils.isBlank(model)) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_MODELISNULL);
        }
        //获取模型数据
        Map data = getData(pageId);
        if (data == null) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_DATAISNULL);
        }
        //静态化页面
        String html = generateHtml(model,data);
        if (StringUtils.isBlank(html)) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_HTMLISNULL);
        }
        return html;
    }

    private String generateHtml(String model, Map data) {
        Configuration configuration = new Configuration(Configuration.getVersion());
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("model",model);
        configuration.setTemplateLoader(templateLoader);
        try {
            Template template = configuration.getTemplate("model");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
        } catch (IOException | TemplateException e) {
            LOG.error("================",e);
        }
        return null;
    }

    private Map getData(String pageId) {
        CmsPage byId = findById(pageId);
        if (byId == null) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_PAGE_NOT_EXIST);
        }
        String dataUrl = byId.getDataUrl();
        if (StringUtils.isBlank(dataUrl)) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_DATAISNULL);
        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        return forEntity.getBody();
    }


    private String getModel(String pageId) {

        CmsPage byId = findById(pageId);
        if (byId == null) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_PAGE_NOT_EXIST);
        }
        String templateId = byId.getTemplateId();
        if (StringUtils.isBlank(templateId)) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_MODELISNULL);
        }
        Optional<CmsTemplate> templateOptional = cmsTemplateRepository.findById(templateId);
        if (templateOptional.isPresent()) {
            CmsTemplate cmsTemplate = templateOptional.get();
            String templateFileId = cmsTemplate.getTemplateFileId();
            GridFSFile one = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            assert one != null;
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(one.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(one, gridFSDownloadStream);
            try {
                return IOUtils.toString(gridFsResource.getInputStream(),"utf-8");
            } catch (IOException e) {
                LOG.error("===========",e);
                return null;
            }
        }else {
            return null;
        }
    }


}
