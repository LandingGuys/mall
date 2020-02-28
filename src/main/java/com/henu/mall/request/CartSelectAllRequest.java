package com.henu.mall.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lv
 * @date 2020-02-27 19:08
 */
@Data
public class CartSelectAllRequest {
    @NotNull
    private Boolean selectAll;
}
