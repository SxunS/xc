package com.xuecheng.cms.client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    /**
     * 队列bean的名称
     */
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";

    /**
     * 交换机的名称
     */
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    /**
     * 队列名称
     */
    @Value("${xuecheng.mq.queue}")
    public String queueCmsPostpageName;

    /**
     * routingKey  即站点id
     */
    @Value("${xuecheng.mq.routingKey}")
    public String routingKey;

    /**
     * 交换机配置适用direct类型
     * @return the direct exchange
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange exchangeTopicsInform(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    /**
     * 声明队列
     * @return the queue
     */
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue queueCmsPostpage(){
        return new Queue(queueCmsPostpageName);
    }

    /**
     * 绑定队列到交换机
     * @param queue 队列
     * @param exchange 交换机
     * @return binding
     */
    @Bean
    public Binding bindingQueueInformSMS(@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue, @Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
