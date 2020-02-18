package com.henu.mall.service;

import com.henu.mall.vo.CategoryVO;
import com.henu.mall.vo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
 * @author lv
 * @date 2020-01-28 14:42
 */
public interface CategoryService {
    /**
     * 查询所有
     * @return
     */
    ResponseVo<List<CategoryVO>> searchAll();

    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
