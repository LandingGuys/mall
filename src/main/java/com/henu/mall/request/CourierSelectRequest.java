package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-04-12 12:59
 */
@Data
public class CourierSelectRequest {

    private String expCode; //快递公司编码

    private String expNo; //快递单号

}
