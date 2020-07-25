package com.henu.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lv
 * @date 2020-03-25 9:51
 */
@Getter
@AllArgsConstructor
public enum EmailTypeEnum {
    EMAIL_REGISTER(0,"邮箱注册"),
    EMAIL_VERIFY(1,"邮箱验证")
    ;
    private Integer type;
    private String msg;
}
