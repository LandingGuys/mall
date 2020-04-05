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

    USERNAME_EXIST(2, "用户名已存在,请重新填写用户名"),

    PARAM_ERROR(3, "参数错误"),

    EMAIL_EXIST(4, "邮箱已存在"),

    ADD_USER_ERROR(5,"添加用户失败"),

    USER_NOT_EXIST(6,"用户不存在"),

    UPDATE_USER_ERROR(7,"修改用户失败"),

    UPLOAD_USER_ERROR(8,"修改头像失败"),

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

    PRODUCT_STOCK_ERROR(20,"商品库存不够"),

    CART_PRODUCT_NOT_EXIST(21,"购车中无该商品"),

    SHIPPING_NOT_EXIST(22,"收货地址不存在"),

    CART_SELECTED_IS_EMPTY(23,"请选择商品后下单"),

    ORDER_NOT_EXIST(24,"订单不存在"),

    ORDER_STATUS_ERROR(25,"订单状态有误"),

    FILE_UPLOAD_ERROR(26,"图片上传失败"),

    USER_DELETE_ERROR(27,"用户删除失败"),

    PRODUCT_ADD_ERROR(28,"添加商品失败"),

    PRODUCT_UPDATE_ERROR(29,"更新商品信息失败"),

    PRODUCT_DELETE_ERROR(30,"删除商品失败"),

    CATEGORY_ADD_ERROR(31,"添加类目失败"),

    CATEGORY_UPDATE_ERROR(32,"更新类目失败"),

    CATEGORY_DELETE_ERROR(32,"删除类目失败"),

    CATEGORY_NOT_EXIST(33,"类目不存在"),

    PHONE_OR_EMAIL_ERROR(34,"请输入正确的手机号或邮箱"),

    VERIFY_CODE_REDIS_NOT_EXIST(35,"验证码不存在，或已过期，请重新获取验证码！"),

    USER_PERMISSION_ERROR(36,"您不是管理员，没有权限呢！！！"),

    REQUEST_MSG_ERROR(37,"请求信息异常"),

    REQUEST_MORE_ERROR(38,"您提交的太频繁了，请您稍后再试"),

    ORDER_DELETE_ERROR(39,"删除订单失败"),

    ORDER_DELETE_STATUS_ERROR(40,"当前订单未取消或未完成，不能删除！"),

    QT_ADMIN_LOGIN_ERROR(41,"管理员账户想登录商城，也需要重新注册！！！"),

    ;

    private Integer code;
    private String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
