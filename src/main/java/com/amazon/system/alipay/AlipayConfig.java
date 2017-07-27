package com.amazon.system.alipay;

/**
 * Created by User on 2017/6/25.
 */
public class AlipayConfig {

    //支付宝网关（固定）
    public static String URL = "https://openapi.alipay.com/gateway.do";

    //APPID 即创建应用后生成
    public static String APPID = "2017062207541994";

    //开发者私钥，由开发者自己生成
    public static String APP_PRIVATE_KEY =
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDfFu22LLfu+80q\n" +
                    "t03dvdh/wAfHx5piZ2NtAbJWSE6DeKleHBpbfPm1S4ripzEBtdGJ7L96eW3gIaPy\n" +
                    "cxR+DNAIDqoMl1C5IYrOaRhw6gUIinLvgTp6/nvzgrNWbr1/vZ6WgjOuZvL8p1n/\n" +
                    "Txavu4GvRAc4Xhbtn8mY/tDfd2iPpqCN2M363ir0w+GNC607dkThCX2Z7XRVwVZq\n" +
                    "RBIdWSzvCZa7FjDNWHwSNKkNhzVMD1/jTUgewCdm/CuKYDHbzpbUDdQ75Vwh4VSy\n" +
                    "jL8u+fqUH/0fZ1bcWRM0klFwemIj3f4wrrcm+66QJ5aN2Njd7yQn1A34YWLDDIlc\n" +
                    "KW4xeYZxAgMBAAECggEBAKqgcc2tJD0v77rlGjQn+Me0yv6dBtKOuvfrnUfq48og\n" +
                    "ZoHpQquIycBE+APk1lG1i39PSSBWcZhEgo10QU8rY41WEe+xZ/dA1VaMATqTcqgW\n" +
                    "pFPU5jKeI5Wa+Kl554wlLn91NCwG54AgS3A/QF3GLykrAc62ABlc5icTdhQE0z9S\n" +
                    "R9e5XFTmm5humNlxMDWfLF4bk/Z8UIXk0g8qN6MuM8WZKL8byu1FLHxgiAP8ay8C\n" +
                    "BbFLSX+Kh08imCCs5OVapTS3WE8+8Pmo8nMtGRE6cREC622hIEtL1Esnh+zASlrR\n" +
                    "PalUEVEKbpv+87vdJO1x83WocvliM+3+BlDLqiRZPlUCgYEA7085vBzU3YLDZ6W3\n" +
                    "C+rZa6tJkA8/lB/P/MRSVZn3Yh8ouugT6Mn5Sft1y8bwchpaOhvOfVU7WPxcBlSL\n" +
                    "AQbnqz2o/gMeaFoyKd0afc5/PvD59r0J8gr/q7AEfop0rUosQlxgl6g5SkGDXG3U\n" +
                    "ewNHSqOmB1YtGJx5GvFZb8GplBcCgYEA7qYaYjU2ROfkH9+Gy47KkYdNYe4a+fnD\n" +
                    "M5Q4hL1+Lv8a86H/ReDGY3Z3GHSotqn4vkoxzolppEdItUybFoSWii+EH6KePxp/\n" +
                    "l8fWfRljpuES5Bi51cmfEbDa4u2+Sp6QlybAqivYP1ldJBARWvnKO2dN0LEfH6mi\n" +
                    "ACxm9sfs5rcCgYEAql18fK8J26h79A3FgnZu0nfepvA0Ev+S0h/xfX9e3jvQgz80\n" +
                    "7UTcdA7QjYHTK2gXU8b3+cHzviXqXZdSXijMRNxgsnUF8MRFcgg2E9ouPGDRkxaB\n" +
                    "Pg8WdOOaT3vCFJv5ZkTdfxoDrxl0qxiFbA2pczmNuStgb2T10nh/c1ArqYECgYEA\n" +
                    "zqEzJuU7392IP7CiXY19BxRHhAtNgDMpOHeVmBkaMEbhZyFUjYRZQbVI2PvpOH9i\n" +
                    "yCAlj9aaiYrPAWsavRx/25XYqCt9KNYgs6JrrFVXWx515Nkjzl+MQx0F6IZAxQsV\n" +
                    "1knPd4gO7IrLgnYHEOZGm7iOP6ucbyXJBq1rWTExhXECgYBmUgeEMxt457k+Jlp4\n" +
                    "Vo+n/o46HTsuNJohJrWCO2T7YTSOtIbUpIarYO2LeEOOig4fQGlClQk5DNJhMNLC\n" +
                    "pxkwlOtF4OK7+4dlqRluIH4hiQePkIA7ZckZuNm1v8LsHVEZhsfvGc1901tOfgL4\n" +
                    "9r9656G+ua1Bqgfrz3V2wYJwmQ==";

    //参数返回格式，只支持json
    public static String FORMAT = "json";

    //编码集，支持GBK/UTF-8
    public static String CHARSET = "utf-8";

    //支付宝公钥，由支付宝生成
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlthvvrj+Ozv+8oPO4cjish/heF1SLMQrAUkQw6SFB08WBoOR7K3aTYtqr49GLaWqLw/ZRxKb9ZggiyPqP5qf+sMstplCjQwV1GgauSE3i/76W6vx5GbJCImfXDLHkkEb7+xe+qJJqVEZ9eGkX1DYUTG14mBcoYKi2hAe7ze1EgGoGr1f5r1SGYfyW9mc56J/3ADoh6iDeW02cJzE03LqzLedWhdY/yHEuCw3RiEUuaV1IMkDFM+wExVYoGQTHv95Medwu8C6Es7Eg5yCnajPVwcjH3jRz1VhocYDAJs5yPRSISggwmyItEi9oJpapo5ra6aKtRRcFidgAaTUsOx1OQIDAQAB";

    //签名方式
    public static String SIGN_TYPE = "RSA2";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String RETURN_URL = "http://www.amreviewtracker.com/mainController.do?index";
    // public static String RETURN_URL = "http://localhost:8888/mainController.do?index";
    public static String RETURN_URL_ORDER = "http://www.amreviewtracker.com/promotOrderController.do?goNewPromotTwo";
    //public static String RETURN_URL_ORDER = "http://localhost:8888/promotOrderController.do?goNewPromotTwo";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String NOTIFY_URL = "http://www.amreviewtracker.com/userFundController/doAlipayTradePageNotify.do";

}
