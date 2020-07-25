package com.henu.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lv
 * @date 2020-02-02 19:41
 */
@Getter
@AllArgsConstructor
public enum  SaleEnum {
    ON_SALE(1,"在售"),
    SOLD_OUT(2,"下架"),
    DELETE(3,"删除")
    ;
    private Integer status;

    private String msg;
}
