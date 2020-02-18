package com.henu.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lv
 * @date 2020-02-15 10:53
 */
@Getter
@AllArgsConstructor
public enum  OrderStatusEnum {

    CANCELED(0, "已取消"),

    NO_PAY(10, "未付款"),

    PAID(20, "已付款"),

    SHIPPED(40, "已发货"),

    TRADE_SUCCESS(50, "交易成功"),

    TRADE_CLOSE(60, "交易关闭"),
    ;

    Integer code;

    String desc;
}
