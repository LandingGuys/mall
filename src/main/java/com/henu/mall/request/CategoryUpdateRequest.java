package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-03-12 9:36
 */
@Data
public class CategoryUpdateRequest {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

}
