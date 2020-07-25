package com.henu.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lv
 * @date 2020-03-25 22:16
 */
@Data
public class CategoryAdminVo {

    private Integer value;

    private String label;

    private List<CategoryAdminVo> children;
}
