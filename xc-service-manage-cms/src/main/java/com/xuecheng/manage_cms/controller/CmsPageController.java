package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    PageService pageService;

    @Override
    @RequestMapping(value = "/list/{pageNo}/{pageSize}",method = RequestMethod.GET)
    public QueryResponseResult findList(@PathVariable("pageNo") int pageNo,@PathVariable("pageSize") int pageSize, QueryPageRequest queryPageRequest) {
        return pageService.findList(pageNo,pageSize,queryPageRequest);
    }

    @Override
    public CmsPageResult add(CmsPage cmsPage) {
        return null;
    }


    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
