package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-04-10 17:01
 */
@Data
public class LogisticsAddRequest {

    private Long orderNo;

    private String logisticsCa;

    private String logisticsNo;

}
