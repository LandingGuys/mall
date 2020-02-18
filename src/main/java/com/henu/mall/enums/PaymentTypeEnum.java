package com.henu.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lv
 * @date 2020-02-15 10:49
 */
@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {

    PAY_ONLINE(1,"在线支付");

    private Integer code;
    private String msg;

}
