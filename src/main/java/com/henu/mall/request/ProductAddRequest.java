package com.henu.mall.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lv
 * @date 2020-03-09 8:38
 */
@Data
public class ProductAddRequest {

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

}
