package com.henu.mall.vo;

import com.henu.mall.pojo.Shipping;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author lv
 * @date 2020-02-13 16:24
 */
@Data
public class OrderVo {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private List<OrderItemVo> orderItemVoList;

    private Integer shippingId;

    private Shipping shippingVo;
}
