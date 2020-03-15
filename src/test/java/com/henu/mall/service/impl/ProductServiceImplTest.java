package com.henu.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.henu.mall.MallApplicationTests;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.SaleEnum;
import com.henu.mall.request.ProductAddRequest;
import com.henu.mall.request.ProductSelectCondition;
import com.henu.mall.request.ProductUpdateRequest;
import com.henu.mall.service.ProductService;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author lv
 * @date 2020-02-02 9:42
 */
@Slf4j
public class ProductServiceImplTest extends MallApplicationTests {
    @Resource
    private ProductService productService;



    @Test
    public void list() {

    }

    @Test
    public void add(){
        ProductAddRequest request = new ProductAddRequest();
        request.setName("板蓝根");
        request.setCategoryId(100012);
        request.setPrice(BigDecimal.valueOf(9.9));
        request.setStatus(SaleEnum.ON_SALE.getStatus());
        request.setMainImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583985949981&di=f40759b1c7cc98e06837a6037fc88585&imgtype=0&src=http%3A%2F%2Fyp.gmw.cn%2Fattachement%2Fjpg%2Fsite2%2F20130318%2Feca86ba052ac12b105c61c.jpg");
        request.setSubtitle("复方板蓝根");
        request.setStock(100);
        request.setSubImages("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583985949981&di=f40759b1c7cc98e06837a6037fc88585&imgtype=0&src=http%3A%2F%2Fyp.gmw.cn%2Fattachement%2Fjpg%2Fsite2%2F20130318%2Feca86ba052ac12b105c61c.jpg");

        ResponseVo responseVo = productService.add(request);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
    @Test
    public void delete(){
        Integer productId = 27;
        ResponseVo responseVo = productService.delete(productId);

        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
    @Test
    public void update(){
        Integer productId = 30;
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setId(productId);
        request.setStatus(SaleEnum.SOLD_OUT.getStatus());

        ResponseVo responseVo = productService.update(request);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void selectByCondition(){
        ProductSelectCondition requset = new ProductSelectCondition();
        requset.setQuery("三九");
        ResponseVo<PageInfo> responseVo = productService.getProductListByCondition(requset);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}