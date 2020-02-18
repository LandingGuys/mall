package com.henu.mall.controller;

import com.henu.mall.pojo.User;
import com.henu.mall.request.CartAddRequest;
import com.henu.mall.request.CartUpdateRequest;
import com.henu.mall.service.CartService;
import com.henu.mall.vo.CartVo;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author lv
 * @date 2020-02-03 9:23
 */
@Slf4j
@RestController
public class CartController {
    @Resource
    private CartService cartService;

    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session){
        User user = (User)session.getAttribute("user");
        return cartService.list(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddRequest cartAddRequest,
                                  HttpSession session){
        User user = (User)session.getAttribute("user");
        return cartService.add(user.getId(),cartAddRequest);
    }
    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@PathVariable Integer productId,@RequestBody CartUpdateRequest cartUpdateRequest,
                                     HttpSession session){
        User user = (User)session.getAttribute("user");
        return cartService.update(user.getId(),productId,cartUpdateRequest);
    }
    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,HttpSession session){
        User user = (User)session.getAttribute("user");
        return cartService.delete(user.getId(),productId);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session){
        User user = (User)session.getAttribute("user");
        return cartService.selectAll(user.getId());
    }
    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession session){
        User user = (User)session.getAttribute("user");
        return cartService.unSelectAll(user.getId());
    }
    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session){
        User user = (User)session.getAttribute("user");
        return cartService.productSum(user.getId());
    }
}
