package com.henu.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lv
 * @date 2020-02-13 16:25
 */
@Data
public class OrderItemVo {

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;
}
