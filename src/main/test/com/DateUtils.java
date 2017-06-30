package com;

import java.util.Date;

/**
 * Created by User on 2017/6/30.
 */
public class DateUtils {

    public static void main(String[] args) {
        Date beginOfDate = org.framework.core.utils.DateUtils.DateUtils.getBeginOfDate();
        beginOfDate = org.framework.core.utils.DateUtils.DateUtils.addMonth(beginOfDate, 10);
        beginOfDate = org.framework.core.utils.DateUtils.DateUtils.getEndOfDate(beginOfDate);
        System.out.println(beginOfDate);
    }


}
