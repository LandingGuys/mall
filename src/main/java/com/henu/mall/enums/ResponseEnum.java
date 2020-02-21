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

    TOKEN_EXPIRE_ERROR(9,"Token过期，请重新登录"),

    NEED_LOGIN(10, "用户未登录, 请先登录"),

    THIRD_PARTY_LOGIN_ERROR(11,"第三方登录失败"),

    THIRD_PARTY_LOGIN_QQ_ERROR(12,"qq授权登录失败"),

    THIRD_PARTY_LOGIN_BAI_DU_ERROR(13,"百度授权登录失败"),

    THIRD_PARTY_LOGIN_WEI_BO_ERROR(14,"微博授权登录失败"),

    USERNAME_OR_PASSWORD_ERROR(15, "用户名或密码错误"),

    USER_TOKEN_ERROR(16,"token错误，请重新登录"),

    GET_USER_INFO_ERROR(17,"获取用户失败"),

    PRODUCT_OFF_SALE_OR_DELETE(18,"商品下架或删除了"),

    PRODUCT_NOT_EXIST(19,"商品不存在"),

    PRODUCT_STOCK_ERROR(20,"商品库存错误"),

    CART_PRODUCT_NOT_EXIST(21,"购车中无该商品"),

    SHIPPING_NOT_EXIST(22,"收货地址不存在"),

    CART_SELECTED_IS_EMPTY(23,"请选择商品后下单"),

    ORDER_NOT_EXIST(24,"订单不存在"),

    ORDER_STATUS_ERROR(25,"订单状态有误"),

    ;

    private Integer code;
    private String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
