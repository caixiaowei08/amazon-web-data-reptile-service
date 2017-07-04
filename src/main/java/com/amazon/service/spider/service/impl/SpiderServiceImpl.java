package com.amazon.service.spider.service.impl;

import com.amazon.service.fund.controller.UserFundController;
import com.amazon.service.spider.pojo.AmazonEvaluateReviewPojo;
import com.amazon.service.spider.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.utils.ContextHolderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Spider;

/**
 * Created by User on 2017/6/14.
 */
@Service("spiderService")
public class SpiderServiceImpl implements SpiderService{

    private static Logger logger = LogManager.getLogger(SpiderServiceImpl.class.getName());

    public boolean spiderAmazonPageInfoSaveToHttpSession(String pageUrl,int threadNum) {
        AmazonOrderPipeline amazonOrderPipeline = new AmazonOrderPipeline();
        amazonOrderPipeline.setSession(ContextHolderUtils.getSession());
        try {
            Spider.create(new AmazonPageProcessor())
                    .addUrl(pageUrl)
                    .addPipeline(amazonOrderPipeline)
                    .thread(threadNum)
                    .run();
        }catch (Exception e){
            logger.error(e.fillInStackTrace());
            return false;
        }
        return true;
    }

    public AmazonEvaluateReviewPojo spiderAmazonEvaluateReviewPojo(String pageUrl, int threadNum) {
        AmazonEvaluateReviewPipeline amazonEvaluateReviewPipeline = new AmazonEvaluateReviewPipeline();
        try {
            Spider.create(new AmazonEvaluateReviewProcessor())
                    .addUrl(pageUrl)
                    .addPipeline(amazonEvaluateReviewPipeline)
                    .thread(threadNum)
                    .run();
        }catch (Exception e){
            logger.error(e.fillInStackTrace());
        }
        return amazonEvaluateReviewPipeline.getAmazonEvaluateReviewPojo();
    }
}
