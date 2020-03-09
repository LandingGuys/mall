package com.henu.mall.service;


import com.github.pagehelper.PageInfo;
import com.henu.mall.request.ProductAddRequest;
import com.henu.mall.request.ProductUpdateRequest;
import com.henu.mall.vo.ProductDetailVo;
import com.henu.mall.vo.ResponseVo;

/**
 * @author lv
 * @date 2020-02-01 21:56
 */
public interface ProductService {
    /**
     * 商品列表
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    /**
     * 商品详情
     * @param productId
     * @return
     */
    ResponseVo<ProductDetailVo> detail(Integer productId);

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

//    /**
//     * 商品上下架
//     * @param request
//     * @return
//     */
//    ResponseVo<ProductDetailVo> UpOrOff(ProductUpdateRequest request);

    /**
     * 删除商品
     * @param productId
     * @return
     */
    ResponseVo delete(Integer productId);
}
