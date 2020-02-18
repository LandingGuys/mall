package com.henu.mall.service;


import com.github.pagehelper.PageInfo;
import com.henu.mall.vo.ProductDetailVo;
import com.henu.mall.vo.ResponseVo;

/**
 * @author lv
 * @date 2020-02-01 21:56
 */
public interface ProductService {
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer productId);
}
