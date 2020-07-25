package com.henu.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lv
 * @date 2020-02-01 21:59
 */
@Data
public class ProductVO {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;
}
