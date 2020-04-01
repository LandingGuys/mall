package com.henu.mall.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lv
 * @date 2020-02-03 10:23
 */
@Data
public class CartAddRequest {
    @NotNull
    private Integer productId;

    private Integer productNum = 1;

    private Boolean selected = true;
}
