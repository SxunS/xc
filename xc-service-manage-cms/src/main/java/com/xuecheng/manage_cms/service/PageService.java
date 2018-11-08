package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository pageRepository;

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
}
