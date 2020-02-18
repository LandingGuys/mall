package com.henu.mall.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author lv
 * @date 2020-02-11 19:23
 */
@Component
public class Md5TokenGenerator implements TokenGenerator {
    @Override
    public String generate(String... strings) {
        long timestamp = System.currentTimeMillis();
        String tokenMeta = "";
        for (String s : strings) {
            tokenMeta = tokenMeta + s;
        }
        tokenMeta = tokenMeta + timestamp;
        String token = DigestUtils.md5DigestAsHex(tokenMeta.getBytes());
        return token;
    }
}
