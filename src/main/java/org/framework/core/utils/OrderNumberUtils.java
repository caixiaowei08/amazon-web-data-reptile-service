package org.framework.core.utils;

import org.framework.core.utils.DateUtils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 2017/6/27.
 */
public class OrderNumberUtils {

    private static int  randomNum = 10000;

    public static String generateOrderNumber() {
        Date now = new Date();
        String date = new SimpleDateFormat("yyyyMMdd").format(now);
        String seconds = new SimpleDateFormat("HHmmssSSS").format(now);
        return date+seconds+generateRiseNumber();
    }

    //自增数据
    private static int generateRiseNumber() {
        synchronized (OrderNumberUtils.class){
            if(randomNum == 99999){
                randomNum = 10000;
            }
            randomNum++;
            return randomNum;
        }
    }
}
