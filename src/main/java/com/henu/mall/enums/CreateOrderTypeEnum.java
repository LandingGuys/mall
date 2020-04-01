package com.henu.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lv
 * @date 2020-04-01 17:29
 */
@Getter
@AllArgsConstructor
public enum CreateOrderTypeEnum {
    CART(0,"购物车下单"),
    BUY_NOW(1,"立即购买")
    ;
    private Integer type;
    private String msg;
}
