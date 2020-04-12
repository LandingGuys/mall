package com.henu.mall.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author lv
 * @date 2020-04-10 16:42
 */
@Data
public class Logistics {

    private Integer id;

    private Long orderNo;

    private String logisticsCa;

    private String logisticsNo;

    private Date createTime;

    private Date updateTime;
}
