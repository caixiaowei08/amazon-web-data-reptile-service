package com.amazon.service.spider.service;

import com.amazon.service.spider.SpiderConstant;
import com.amazon.service.spider.pojo.AmazonEvaluateReviewPojo;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;


/**
 * Created by User on 2017/6/24.
 */
public class AmazonEvaluateReviewPipeline implements Pipeline {

    AmazonEvaluateReviewPojo amazonEvaluateReviewPojo = null;

    public void process(ResultItems resultItems, Task task) {
        amazonEvaluateReviewPojo = (AmazonEvaluateReviewPojo)resultItems.get(SpiderConstant.AMAZON_EVALUATE_REVIEW_POJO);
    }

    public AmazonEvaluateReviewPojo getAmazonEvaluateReviewPojo() {
        return amazonEvaluateReviewPojo;
    }

    public void setAmazonEvaluateReviewPojo(AmazonEvaluateReviewPojo amazonEvaluateReviewPojo) {
        this.amazonEvaluateReviewPojo = amazonEvaluateReviewPojo;
    }

}
