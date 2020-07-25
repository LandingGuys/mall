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
     * 后台管理查询所有 树型列表
     * @return
     */
    ResponseVo<List<CategoryAdminVo>> adminSelect();

    /**
     * 后台管理查询所有 表格
     * @return
     */
    ResponseVo adminSelectAll(Integer categoryId,Integer pageNum,Integer pageSize,String query);

    /**
     * 根据类目id 查询类目信息
     * @param id
     * @return
     */
    ResponseVo getCategoryById(Integer id);

    /**
     * 根据类目id,删除类目信息
     * @param id
     * @return
     */
    ResponseVo delete(Integer id);
}
