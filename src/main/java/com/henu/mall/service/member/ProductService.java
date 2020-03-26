package com.henu.mall.service.member;


import com.github.pagehelper.PageInfo;
import com.henu.mall.request.ProductSelectCondition;
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
     * 根据查询条件查询商品
     * @param condition
     * @return
     */
    ResponseVo<PageInfo> getProductListByCondition(ProductSelectCondition condition);


    /**
     * 热门或新品
     * @param type
     * @return
     */
    ResponseVo hotOrNew(String type);
}
