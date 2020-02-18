package com.henu.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.henu.mall.MallApplicationTests;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.request.CartAddRequest;
import com.henu.mall.request.CartUpdateRequest;
import com.henu.mall.service.CartService;
import com.henu.mall.vo.CartVo;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-02-10 14:18
 */
@Slf4j
public class CartServiceImplTest extends MallApplicationTests {
    @Resource
    private CartService cartService;
    private  Integer uid =1;
    private Integer productId =28;
    @Test
    public void add() {
        log.info("【新增购物车...】");
        CartAddRequest form = new CartAddRequest();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid, form);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
    @Test
    public void list(){
        ResponseVo<CartVo> responseVo = cartService.list(uid);
        log.info("list={}",JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update() {
        log.info("【更新购物车...】");
        CartUpdateRequest request =new CartUpdateRequest();
        request.setQuantity(2);
        request.setSelected(false);
        ResponseVo<CartVo> responseVo = cartService.update(uid,productId,request);
        log.info("list={}",JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void delete() {
        log.info("【删除购物车...】");
        ResponseVo<CartVo> responseVo = cartService.delete(uid, productId);
        log.info("result={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void selectAll() {
        ResponseVo<CartVo> responseVo = cartService.selectAll(uid);
        log.info("result={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void unSelectAll() {
        ResponseVo<CartVo> responseVo = cartService.unSelectAll(uid);
        log.info("result={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void productSum() {
        ResponseVo<Integer> responseVo = cartService.productSum(uid);
        log.info("result={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}