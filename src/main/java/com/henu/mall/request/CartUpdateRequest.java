package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-02-12 19:05
 */
@Data
public class CartUpdateRequest {

    private Integer productId;

    private Integer quantity;

    private Boolean selected;
}
