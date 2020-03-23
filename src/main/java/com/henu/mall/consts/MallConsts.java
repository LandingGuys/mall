package com.henu.mall.consts;

/**
 * @author lv
 * @date 2020-01-31 13:06
 */
public class MallConsts {
    public static final String CURRENT_USER = "currentUser";

    public static final Integer ROOT_PARENT_ID = 0;

    public static final String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    public static final String USER_TOKEN_KEY_TEMPLATE = "token_%s";

    public static final String USER_TOKEN_KEY_RESET_TEMPLATE = "token_rest_%s";

    public static final String EMAIL_KEY_TEMPLATE = "email_%s";

    public static final String PHONE_KEY_TEMPLATE = "phone_%s";

    public static  final String HTTP_HEADER_NAME = "Authorization";
    // 用户token 过期时间 15 分钟 单位 秒
    public static final Integer USER_TOKEN_KEY_EXPIRE_TIME = 60 * 15;

    public static final Integer USER_TOKEN_KEY_RESET_TIME = 1000 * 100;
    //订单超时时间 30分钟 单位毫秒
    public static final Long ORDER_TIME_OUT_TIME = 1000*60*30L;

}
