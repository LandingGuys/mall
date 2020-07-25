package com.henu.mall.annotation;

import java.lang.annotation.*;

/**
 * @author lv
 * @date 2020-02-22 9:00
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {
}
