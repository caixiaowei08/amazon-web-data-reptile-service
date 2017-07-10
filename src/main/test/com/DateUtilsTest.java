package com;

import org.framework.core.utils.DateUtils.DateUtils;

import java.util.Date;

/**
 * Created by User on 2017/6/30.
 */
public class DateUtilsTest {

    public static void main(String[] args) {
   /*     Date beginOfDate = org.framework.core.utils.DateUtils.DateUtils.getBeginOfDate();
        beginOfDate = org.framework.core.utils.DateUtils.DateUtils.addMonth(beginOfDate, 10);
        beginOfDate = org.framework.core.utils.DateUtils.DateUtils.getEndOfDate(beginOfDate);
        System.out.println(beginOfDate);*/
        try {
            System.out.println(DateUtils.daysBetween(new Date(), DateUtils.addDay(new Date(),10)));
        } catch (Exception e) {

        }

    }
}
