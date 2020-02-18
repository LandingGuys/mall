package com.henu.mall.service;

import com.henu.mall.pojo.Cart;
import com.henu.mall.request.CartAddRequest;
import com.henu.mall.request.CartUpdateRequest;
import com.henu.mall.vo.CartVo;
import com.henu.mall.vo.ResponseVo;

import java.util.List;

/**
 * @author lv
 * @date 2020-02-03 9:37
 */
public interface CartService {

     ResponseVo<CartVo> list(Integer uid);

     ResponseVo<CartVo> add(Integer uid, CartAddRequest cartAddRequest);

     ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateRequest cartUpdateRequest);

     ResponseVo<CartVo> delete(Integer uid,Integer productId);

     ResponseVo<CartVo> selectAll(Integer uid);

     ResponseVo<CartVo> unSelectAll(Integer uid);

     ResponseVo<Integer> productSum(Integer uid);

     List<Cart> listForCart(Integer uid);

}
