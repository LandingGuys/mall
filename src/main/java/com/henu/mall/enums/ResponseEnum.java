package com.henu.mall.enums;


import lombok.Getter;

/**
 * @author lv
 * @date 2019-12-22 17:30
 */
@Getter
public enum ResponseEnum {
    ERROR(-1,"服务端错误"),

    SUCCESS(0,"成功"),

    PASSWORD_ERROR(1,"密码错误"),

    USERNAME_EXIST(2, "用户名已存在"),

    PARAM_ERROR(3, "参数错误"),

    EMAIL_EXIST(4, "邮箱已存在"),

    NEED_LOGIN(10, "用户未登录, 请先登录"),

    THIRD_PARTY_LOGIN_QQ_ERROR(11,"qq授权登录失败"),

    THIRD_PARTY_LOGIN_WEI_XIN_ERROR(11,"微信授权登录失败"),

    THIRD_PARTY_LOGIN_WEI_BO_ERROR(11,"微博授权登录失败"),

    USERNAME_OR_PASSWORD_ERROR(11, "用户名或密码错误"),;

    private Integer code;
    private String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
