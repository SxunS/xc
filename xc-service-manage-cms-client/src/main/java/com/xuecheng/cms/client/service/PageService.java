package com.xuecheng.cms.client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.cms.client.dao.CmsPageRepository;
import com.xuecheng.cms.client.dao.CmsSiteRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository pageRepository;

    @Autowired
    private CmsSiteRepository siteRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 将页面html 保存到页面物理路径
     * @param pageId 页面id
     */
    public void savePageToRealPath(String pageId){
        Optional<CmsPage> pageById = pageRepository.findById(pageId);
        if (!pageById.isPresent()) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_PAGE_NOT_EXIST);
        }
        CmsPage cmsPage = pageById.get();
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = getSiteById(siteId);
        //页面物理路径
        String pagePath = cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        String htmlFileId = cmsPage.getHtmlFileId();
        InputStream is = getFileById(htmlFileId);
        if (is == null) {
            ExceptionCast.cast(CommonCode.CMS_GENERATE_HTMLISNULL);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pagePath);
            IOUtils.copy(is,fos);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public CmsPage findById(String id){
        Optional<CmsPage> byId = pageRepository.findById(id);
        return byId.orElse(null);
    }

    private InputStream getFileById(String htmlFileId) {
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        assert file != null;
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(file.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(file, gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CmsSite getSiteById(String siteId) {
        Optional<CmsSite> siteById = siteRepository.findById(siteId);
        return siteById.orElse(null);
    }

}
