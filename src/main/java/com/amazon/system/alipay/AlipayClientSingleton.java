package com.amazon.system.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

/**
 * Created by User on 2017/6/25.
 */
public class AlipayClientSingleton {
        private static volatile AlipayClient alipayClient = null;
        private AlipayClientSingleton(){}
        public static AlipayClient getInstance() {
            if (alipayClient == null) {
                synchronized (AlipayClientSingleton.class) {
                    if (alipayClient == null) {
                        alipayClient = new DefaultAlipayClient(
                                AlipayConfig.URL,
                                AlipayConfig.APPID,
                                AlipayConfig.APP_PRIVATE_KEY,
                                AlipayConfig.FORMAT,
                                AlipayConfig.CHARSET,
                                AlipayConfig.ALIPAY_PUBLIC_KEY,
                                AlipayConfig.SIGN_TYPE
                               );
                    }
                }
            }
            return alipayClient;
        }

}
