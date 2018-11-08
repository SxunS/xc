package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void pageListTest(){
        PageRequest request = PageRequest.of(1, 10);
        Page<CmsPage> pageRepositoryAll = cmsPageRepository.findAll(request);
        System.out.println(pageRepositoryAll);
    }


    @Test
    public void insertTest(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面------3");
        cmsPage.setPageStatus("-1");
        cmsPage.setPageCreateTime(new Date());
        CmsPageParam cmsPageParam = new CmsPageParam();
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        cmsPageParam.setPageParamName("paramName");
        cmsPageParam.setPageParamValue("paramValue");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        CmsPage save = cmsPageRepository.save(cmsPage);
        System.out.println(save);
        System.out.println("保存成功");
    }

    @Test
    public void deleteTest(){
        try {
            cmsPageRepository.deleteById("5bd0317ca8032d0820a70ec2");
            System.out.println("success");
        } catch (Exception e) {
            System.out.println("fail");
        }
    }

    @Test
    public void updateTest(){
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById("5bd143c9f13b152c80f5f978");
        if (cmsPageOptional.isPresent()) {
            CmsPage cmsPage = cmsPageOptional.get();
            cmsPage.setPageName("<===========>");
            cmsPageRepository.save(cmsPage);
        }
    }
    @Test
    public void findAllTest(){
        //条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching();
        matcher = matcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("分类导航");
        //创建条件按实例
        Example<CmsPage> example = Example.of(cmsPage, matcher);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println("==============================");
        System.out.println(all);
    }
}
