package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-03-12 9:36
 */
@Data
public class CategoryAddRequest {



    private String name;

    private Boolean status;

    private Integer sortOrder;

}
