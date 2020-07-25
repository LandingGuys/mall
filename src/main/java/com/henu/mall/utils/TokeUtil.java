package com.henu.mall.utils;

import java.util.concurrent.TimeUnit;

import static com.henu.mall.consts.MallConsts.USER_TOKEN_KEY_EXPIRE_TIME;
import static com.henu.mall.consts.MallConsts.USER_TOKEN_KEY_TEMPLATE;

/**
 * @author lv
 * @date 2020-02-11 12:02
 */
public class TokeUtil {
    public static void setToken(String username,RedisUtil redisUtil,String token){
        //token 存入 redis 过期时间10分钟
        redisUtil.setEx(username,token,USER_TOKEN_KEY_EXPIRE_TIME,TimeUnit.SECONDS);
        redisUtil.setEx(token,username,USER_TOKEN_KEY_EXPIRE_TIME,TimeUnit.SECONDS);

        String tokenKey = String.format(USER_TOKEN_KEY_TEMPLATE,username);
        Long currentTime = System.currentTimeMillis();

        redisUtil.set(tokenKey,currentTime.toString());



    }

    public static String getToken(String username,RedisUtil redisUtil){
        String tokenKey = String.format(USER_TOKEN_KEY_TEMPLATE,username);
        return redisUtil.get(tokenKey);
    }



}
