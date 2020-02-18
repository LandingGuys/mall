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

    private Boolean selected = true;
}
