package com.henu.mall.utils;

/**
 * @author lv
 * @date 2020-04-12 18:19
 */
public class ExpCodeUtil {

    public static String expCode(String expCode){
        if(expCode.contains("顺丰")){
            return "SF";
        }else if(expCode.contains("中通")){
            return "ZTO";
        }else if(expCode.contains("申通")){
            return "STO";
        }else if(expCode.contains("圆通")){
            return "YTO";
        }else if(expCode.contains("韵达")){
            return "YD";
        }else if(expCode.contains("邮政")){
            return "YZPY";
        }
        return null;
    }
}
