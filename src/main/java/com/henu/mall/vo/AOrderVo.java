package com.henu.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lv
 * @date 2020-04-04 15:48
 */
@Data
public class AOrderVo {

    private Long orderNo;

    private String userName;

    private String receiverName;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer status;

    private Date paymentTime;

    private Date createTime;

    private Date closeTime;

}
