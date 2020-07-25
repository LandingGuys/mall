package com.henu.mall.controller.merber;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.CartAddRequest;
import com.henu.mall.request.CartSelectAllRequest;
import com.henu.mall.request.CartUpdateRequest;
import com.henu.mall.service.member.CartService;
import com.henu.mall.vo.CartVo;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author lv
 * @date 2020-02-03 9:23
 */
@Api(description = "前台购物车接口")
@Slf4j
@RestController
@AuthIgnore
public class CartController {
    @Resource
    private CartService cartService;

    @ApiOperation("获取当前登录用户购物车列表")
    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.list(user.getId());
    }

    @ApiOperation("加入购物车")
    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddRequest cartAddRequest,
                                  HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.add(user.getId(),cartAddRequest);
    }

    @ApiOperation("更新购物车")
    @PutMapping("/carts")
    public ResponseVo<CartVo> update(@RequestBody CartUpdateRequest cartUpdateRequest,
                                     HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.update(user.getId(),cartUpdateRequest);
    }

    @ApiOperation("根据商品id移除购物车")
    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.delete(user.getId(),productId);
    }

    @ApiOperation("购物车全选或全不选")
    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> isSelectAll(HttpSession session, @RequestBody CartSelectAllRequest cartSelectAllRequest){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.isSelectAll(user.getId(),cartSelectAllRequest.getSelectAll());
    }

    @ApiOperation("统计购物车商品数量")
    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return cartService.productSum(user.getId());
    }
}
