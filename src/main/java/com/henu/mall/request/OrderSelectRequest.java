package com.henu.mall.request;

import lombok.Data;

import java.util.Date;

/**
 * @author lv
 * @date 2020-04-04 14:52
 */
@Data
public class OrderSelectRequest {

    private Integer pageNum;

    private Integer pageSize;

    private Long orderNo;

    private String receiverNameOrPhone;

    private Date time;
}
