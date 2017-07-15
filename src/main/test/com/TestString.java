package com;

/**
 * Created by User on 2017/7/15.
 */
public class TestString {

    public static void main(String[] args) {
        String reviewUrl = "https://www.amazon.com/gp/aw/review/B01CJ6SO6O/R27F2QIMFNUZLS?ref_=glimp_1rv_cl";

        String reviewCode = reviewUrl.substring(reviewUrl.lastIndexOf("/") + 1, reviewUrl.lastIndexOf("?"));

        System.out.println(reviewCode);
    }
}
