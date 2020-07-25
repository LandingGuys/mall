package com.henu.mall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lv
 * @date 2020-02-10 13:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;
}
