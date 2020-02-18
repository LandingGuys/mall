package com.henu.mall.utils;

import org.springframework.stereotype.Component;

/**
 * @author lv
 * @date 2020-02-11 19:20
 */
@Component
public interface TokenGenerator {

    public String generate(String... strings);
}
