package com.henu.mall.controller.merber;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.CartAddRequest;
import com.henu.mall.request.CartSelectAllRequest;
import com.henu.mall.request.CartUpdateRequest;
import com.henu.mall.service.member.CartService;
import com.henu.mall.vo.CartVo;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
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
@AuthIgnore
public class CartController {
    @Resource
    private CartService cartService;

    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.list(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddRequest cartAddRequest,
                                  HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.add(user.getId(),cartAddRequest);
    }
    @PutMapping("/carts")
    public ResponseVo<CartVo> update(@RequestBody CartUpdateRequest cartUpdateRequest,
                                     HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.update(user.getId(),cartUpdateRequest);
    }
    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.delete(user.getId(),productId);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> isSelectAll(HttpSession session, @RequestBody CartSelectAllRequest cartSelectAllRequest){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.isSelectAll(user.getId(),cartSelectAllRequest.getSelectAll());
    }
//    @PutMapping("/carts/unSelectAll")
//    public ResponseVo<CartVo> unSelectAll(HttpSession session){
//        UserVo user =(UserVo) session.getAttribute("user");
//        return cartService.unSelectAll(user.getId());
//    }
    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.productSum(user.getId());
    }
}
