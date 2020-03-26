package com.henu.mall.service.admin;

import com.henu.mall.request.CategoryAddRequest;
import com.henu.mall.request.CategoryUpdateRequest;
import com.henu.mall.vo.CategoryAdminVo;
import com.henu.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author lv
 * @date 2020-03-26 10:46
 */
public interface ACategoryService {

    /**
     * 新增类目
     */
    ResponseVo add(CategoryAddRequest request);

    /**
     * 更新类目
     * @param request
     * @return
     */
    ResponseVo update(CategoryUpdateRequest request);

    /**
     * 后台管理查询所有
     * @return
     */
    ResponseVo<List<CategoryAdminVo>> adminSelectAll();

}
