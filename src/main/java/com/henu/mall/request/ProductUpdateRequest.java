package com.henu.mall.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lv
 * @date 2020-03-09 8:41
 */
@Data
public class ProductUpdateRequest {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private Integer isHot;

    private Integer isNew;
}
