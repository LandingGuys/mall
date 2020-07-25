package com.henu.mall.service.admin.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.henu.mall.MallApplicationTests;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.service.admin.AOrderService;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-04-05 11:21
 */
@Slf4j
public class AOrderServiceImplTest extends MallApplicationTests {
    @Resource
    private AOrderService orderService;

    @Test
    public void list() {
        Integer pageNum=1;

        Integer pageSiz=10;
        ResponseVo<PageInfo> responseVo = orderService.list(null,pageNum,pageSiz,null,null);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void detail() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}