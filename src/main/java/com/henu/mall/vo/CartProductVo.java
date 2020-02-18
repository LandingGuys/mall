package com.henu.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author lv
 * @date 2020-02-03 9:30
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class CartProductVo {

    private Integer productId;

    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    private BigDecimal productTotalPrice;

    private Integer productStock;

    private Boolean productSelected;
}
