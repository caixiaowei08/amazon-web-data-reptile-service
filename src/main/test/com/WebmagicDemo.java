package com;

import us.codecraft.webmagic.Spider;

/**
 * Created by User on 2017/6/14.
 */
public class WebmagicDemo {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Spider.create(new AmazonPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://www.amazon.com/JINRI-Negative-Button-Speeds-Settings/dp/B06VWCK7FH/ref=cm_rdp_product")
                .addPipeline(new AmazonOrderPipeline())
                //开启5个线程抓取
                .thread(2)
                //启动爬虫
                .run();
        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }
}
