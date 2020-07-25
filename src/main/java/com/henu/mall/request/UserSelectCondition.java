package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-02-23 10:33
 */
@Data
public class UserSelectCondition {

    private String query;

    private Integer pageNum =1;

    private Integer pageSize =10;

}
