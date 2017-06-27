package org.framework.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 2017/6/13.
 */
public class RegularExpressionUtils {

    public static boolean isHttpOrHttps(String url){
        Pattern pattern = Pattern .compile("(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static boolean isMoney(String money){
        Pattern pattern = Pattern .compile("^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)(?:\\.\\d{1,2})?$");
        Matcher matcher = pattern.matcher(money);
        return matcher.matches();
    }




}
