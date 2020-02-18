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

    public static final Integer USER_TOKEN_KEY_EXPIRE_TIME = 60 * 10;

    public static final Integer USER_TOKEN_KEY_RESET_TIME = 1000 * 100;

}
