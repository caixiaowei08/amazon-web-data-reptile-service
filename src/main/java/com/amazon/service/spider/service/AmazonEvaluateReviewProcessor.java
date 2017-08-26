package com.amazon.service.spider.service;

import com.amazon.service.spider.SpiderConstant;
import com.amazon.service.spider.pojo.AmazonEvaluateReviewPojo;
import com.amazon.service.spider.pojo.AmazonPageInfoPojo;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by User on 2017/6/24.
 */
public class AmazonEvaluateReviewProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    public void process(Page page) {
        AmazonEvaluateReviewPojo amazonEvaluateReviewPojo = new AmazonEvaluateReviewPojo();
        amazonEvaluateReviewPojo.setReviewUrl(page.getRequest().getUrl());
        amazonEvaluateReviewPojo.setProductUrl(page.getHtml().xpath("//table/tbody/tr[1]/td[@valign='top'][2]/div[1]/div[@class='cBoxInner']/div[@id='rdpItemInfo']/div[@class='crDescription']/div[1]/a/@href").toString());
        amazonEvaluateReviewPojo.setAsin(page.getHtml().xpath("//span[@class='asinReviewsSummary']/@name").toString());
        amazonEvaluateReviewPojo.setReviewCode(page.getHtml().xpath("//table/tbody/tr[1]/td[@valign='top'][1]/a/@name").toString());
        amazonEvaluateReviewPojo.setReviewContent(page.getHtml().xpath("//table/tbody/tr[1]/td[@valign='top'][1]/div[1]/div[1]/b/text()").toString());
        amazonEvaluateReviewPojo.setReviewDate(page.getHtml().xpath("//table/tbody/tr[1]/td[@valign='top'][1]/div[1]/div[1]/nobr/text()").toString());
        amazonEvaluateReviewPojo.setReviewStar(page.getHtml().xpath("//table/tbody/tr[1]/td[@valign='top'][1]/div[1]/div[1]/span[1]/img/@title").toString());

        if (!StringUtils.hasText(amazonEvaluateReviewPojo.getReviewContent())) {
            amazonEvaluateReviewPojo.setReviewContent(page.getHtml().xpath("//table/tbody/tr[1]/td[@valign='top'][1]/div[1]/div[2]/b/text()").toString());
        }
        if (!StringUtils.hasText(amazonEvaluateReviewPojo.getReviewDate())) {
            amazonEvaluateReviewPojo.setReviewDate(page.getHtml().xpath("//table/tbody/tr[1]/td[@valign='top'][1]/div[1]/div[2]/nobr/text()").toString());
        }
        if (!StringUtils.hasText(amazonEvaluateReviewPojo.getReviewStar())) {
            amazonEvaluateReviewPojo.setReviewStar(page.getHtml().xpath("//table/tbody/tr[1]/td[@valign='top'][1]/div[1]/div[2]/span[1]/img/@title").toString());
        }

        //20170820补充
        if (StringUtils.isEmpty(amazonEvaluateReviewPojo.getAsin())) {
            String url = page.getHtml().xpath("//div[@id='cm_cr-review_list']/div[1]/div[1]/div[1]/a[1]/@href").toString();
            if (StringUtils.hasText(url)) {
                if (url.contains("ASIN=")) {
                    amazonEvaluateReviewPojo.setAsin(url.substring(url.indexOf("ASIN=") + 5));
                }
            }
        }

        if (StringUtils.isEmpty(amazonEvaluateReviewPojo.getReviewCode())) {
            amazonEvaluateReviewPojo.setReviewCode(page.getHtml().xpath("//div[@id='cm_cr-review_list']/div/@id").toString());
        }

        if (StringUtils.isEmpty(amazonEvaluateReviewPojo.getReviewContent())) {
            amazonEvaluateReviewPojo.setReviewContent(page.getHtml().xpath("//div[@id='cm_cr-review_list']//a[@data-hook='review-title']/text()").toString());
        }

        if (StringUtils.isEmpty(amazonEvaluateReviewPojo.getReviewStar())) {
            amazonEvaluateReviewPojo.setReviewStar(page.getHtml().xpath("//div[@id='cm_cr-review_list']/div[1]/div[1]/div[1]/a[1]/@title").toString());
        }

        if (StringUtils.isEmpty(amazonEvaluateReviewPojo.getReviewDate())) {
            amazonEvaluateReviewPojo.setReviewDate(page.getHtml().xpath("//div[@id='cm_cr-review_list']/div[1]/div[1]/div[2]/span[4]/text()").toString().substring(3));
        }

        page.putField(SpiderConstant.AMAZON_EVALUATE_REVIEW_POJO, amazonEvaluateReviewPojo);
    }

    public Site getSite() {
        return site;
    }
}
