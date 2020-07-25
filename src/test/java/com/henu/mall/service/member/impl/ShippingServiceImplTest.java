package com.henu.mall.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.henu.mall.MallApplicationTests;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.request.ShippingRequest;
import com.henu.mall.service.member.ShippingService;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lv
 * @date 2020-02-13 9:10
 */
@Slf4j
public class ShippingServiceImplTest extends MallApplicationTests {

    @Resource
    private ShippingService shippingService;

    @Test
    public void add() {
        ShippingRequest shippingRequest = new ShippingRequest();
        shippingRequest.setReceiverName("lvbenwei");
        shippingRequest.setReceiverMobile("18639760874");
        shippingRequest.setReceiverCity("河南信阳光山县");
//        shippingRequest.setIsDefault(1);
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(27, shippingRequest);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }

    @Test
    public void delete() {
    }

    @Test
    public void update() {
        ShippingRequest shippingRequest = new ShippingRequest();
        shippingRequest.setIsDefault(true);
        ResponseVo responseVo = shippingService.update(27, 34, shippingRequest);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }

    @Test
    public void list() {
        ResponseVo<PageInfo> list = shippingService.list(27, 1, 10);
        log.info("list={}", JSON.toJSONString(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), list.getStatus());


    }
}