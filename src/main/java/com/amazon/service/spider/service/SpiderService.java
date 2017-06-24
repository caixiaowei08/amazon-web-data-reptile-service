package com.amazon.service.spider.service;

import com.amazon.service.spider.pojo.AmazonEvaluateReviewPojo;

/**
 * Created by User on 2017/6/14.
 */
public interface SpiderService {

    /**
     * 爬虫
     * 阻塞线程处理网页信息
     * @param pageUrl
     * @return
     */
    public boolean spiderAmazonPageInfoSaveToHttpSession(String pageUrl,int threadNum);

    /**
     * 爬虫
     * 阻塞线程处理网页信息
     * @param pageUrl
     * @return
     */
    public AmazonEvaluateReviewPojo spiderAmazonEvaluateReviewPojo(String pageUrl, int threadNum);

}
