package com.henu.mall.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lv
 * @date 2020-02-19 22:29
 */
@Data
public class OrderCreateRequest {
    @NotNull
    private Integer shippingId;
}
