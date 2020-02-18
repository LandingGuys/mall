package com.henu.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lv
 * @date 2020-02-03 9:29
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private Boolean selectAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;
}
