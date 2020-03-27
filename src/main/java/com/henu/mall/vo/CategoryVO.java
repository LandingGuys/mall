package com.henu.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lv
 * @date 2020-01-28 12:40
 */
@Data
public class CategoryVO {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private List<CategoryVO> subCategories;
}
