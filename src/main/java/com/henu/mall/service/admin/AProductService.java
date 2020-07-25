package com.henu.mall.service.admin;

import com.github.pagehelper.PageInfo;
import com.henu.mall.request.ProductAddRequest;
import com.henu.mall.request.ProductUpdateRequest;
import com.henu.mall.vo.ResponseVo;

/**
 * @author lv
 * @date 2020-03-26 10:41
 */
public interface AProductService {
    /**
     * 商品列表
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize,String query);

    /**
     * 新增商品
     * @param request
     * @return
     */
    ResponseVo add(ProductAddRequest request);

    /**
     * 更新商品
     * @param request
     * @return
     */
    ResponseVo update(ProductUpdateRequest request);

    /**
     * 删除商品
     * @param productId
     * @return
     */
    ResponseVo delete(Integer productId);

    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    ResponseVo detail(Integer productId);

}
