package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository pageRepository;

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
            CmsPage save = pageRepository.save(oldCmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,save);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }
}
