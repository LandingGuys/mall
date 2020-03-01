package com.henu.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.henu.mall.MallApplicationTests;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.request.CartAddRequest;
import com.henu.mall.request.OrderCreateRequest;
import com.henu.mall.service.CartService;
import com.henu.mall.service.OrderService;
import com.henu.mall.vo.CartVo;
import com.henu.mall.vo.OrderVo;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-02-15 12:49
 */
@Slf4j
public class OrderServiceImplTest extends MallApplicationTests {
    @Resource
    private CartService cartService;

    @Resource
    private OrderService orderService;

    private  Integer uid =1;
    private Integer productId =26;
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
    public void createTest() {
        ResponseVo<OrderVo> responseVo = create();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    private ResponseVo<OrderVo> create() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setReceiverName("吕本伟");
        request.setReceiverPhone("18639760875");
        request.setReceiverAddress("河南信阳");
        ResponseVo<OrderVo> responseVo = orderService.create(uid,request);
        log.info("result={}", JSON.toJSONString(responseVo));
        return responseVo;
    }
}