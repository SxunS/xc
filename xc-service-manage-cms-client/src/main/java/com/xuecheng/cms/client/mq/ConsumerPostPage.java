package com.xuecheng.cms.client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.cms.client.service.PageService;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class ConsumerPostPage {
    private static final Logger LOG = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    private PageService pageService;

    @RabbitListener(queues = "${xuecheng.mq.queue}")
    public void postPage(String msg){
        Map map = JSON.parseObject(msg, Map.class);
        LOG.info("receive message is {}",msg);
        String pageId = (String) map.get("pageId");
        CmsPage cmsPage = pageService.findById(pageId);
        if (cmsPage != null) {
            pageService.savePageToRealPath(pageId);
        }else {
            LOG.error("receive post page message,but cmsPage is null:{}",msg);
        }
    }
}
