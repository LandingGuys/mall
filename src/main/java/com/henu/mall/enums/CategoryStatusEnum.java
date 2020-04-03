package com.henu.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lv
 * @date 2020-04-03 21:10
 */
@Getter
@AllArgsConstructor
public enum CategoryStatusEnum {

    ON(1,"正常"),
    OUT(2,"已遗弃"),
    DELETE(3,"删除");

    private Integer status;

    private String msg;
}
