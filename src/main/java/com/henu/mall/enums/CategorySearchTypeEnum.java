package com.henu.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lv
 * @date 2020-03-27 11:44
 */
@Getter
@AllArgsConstructor
public enum CategorySearchTypeEnum {
    ADMIN(0,"后台"),
    MEMBER(1,"前台")
    ;
    private Integer type;
    private String msg;
}
