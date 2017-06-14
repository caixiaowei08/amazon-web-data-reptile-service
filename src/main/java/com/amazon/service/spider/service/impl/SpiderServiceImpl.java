package com.amazon.service.spider.service.impl;

import com.amazon.service.spider.service.AmazonOrderPipeline;
import com.amazon.service.spider.service.AmazonPageProcessor;
import com.amazon.service.spider.service.SpiderService;
import org.framework.core.utils.ContextHolderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

/**
 * Created by User on 2017/6/14.
 */
@Service("spiderService")
public class SpiderServiceImpl implements SpiderService{

    public boolean spiderAmazonPageInfoSaveToHttpSession(String pageUrl,int threadNum) {
        AmazonOrderPipeline amazonOrderPipeline = new AmazonOrderPipeline();
        amazonOrderPipeline.setSession(ContextHolderUtils.getSession());
        try {
            Spider.create(new AmazonPageProcessor())
                    //从"https://github.com/code4craft"开始抓
                    .addUrl(pageUrl)
                    .addPipeline(amazonOrderPipeline)
                    //开启5个线程抓取
                    .thread(threadNum)
                    //启动爬虫
                    .run();
        }catch (Exception e){
            return false;
        }
        return true;
    }
}