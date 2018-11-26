package com.xuecheng.manage_cms.dao.filetest;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.manage_cms.dao.CmsPageRepositoryTest;
import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFSTemplateTest {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private PageService pageService;

    @Test
    public void test() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("C:\\Users\\s_xun\\Desktop\\freemarker.html");
        ObjectId store = gridFsTemplate.store(inputStream, "freeMarker.html", "");
        System.out.println(store);
    }

    @Test
    public void queryFileById() throws IOException {
        String id = "5bf6668b729acf30e414d1f5";
        Criteria criteria = Criteria.where("_id").is(id);
        Query query = Query.query(criteria);
        GridFSFile one = gridFsTemplate.findOne(query);
        assert one != null;
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(one.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(one, gridFSDownloadStream);
        String s = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(s);
    }

    public void generateHtmlTest(){
        String html = pageService.getHtml("5ad92e9068db52404cad0f79");
        System.out.println(html);
    }

}
