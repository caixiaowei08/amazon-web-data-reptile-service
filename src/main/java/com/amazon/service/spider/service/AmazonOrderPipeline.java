package com.amazon.service.spider.service;

import com.amazon.service.spider.SpiderConstant;
import com.amazon.service.spider.pojo.AmazonPageInfoPojo;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.servlet.http.HttpSession;

/**
 * Created by User on 2017/6/14.
 */
public class AmazonOrderPipeline implements Pipeline {

    private HttpSession session;

    public void process(ResultItems resultItems, Task task) {
        AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo)resultItems.get(SpiderConstant.AMAZON_PAGE_INFO_POJO);
        session.setAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO,amazonPageInfoPojo);
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }
}
