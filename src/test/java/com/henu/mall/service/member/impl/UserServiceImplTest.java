package com.henu.mall.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.henu.mall.MallApplicationTests;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.request.UserAddRequest;
import com.henu.mall.request.UserSelectCondition;
import com.henu.mall.request.UserUpdateRequest;
import com.henu.mall.service.admin.AUserService;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-02-23 10:53
 */
@Slf4j
public class UserServiceImplTest extends MallApplicationTests {
    @Resource
    private AUserService userService;

    @Test
    public void getUserListByCondition() {
        UserSelectCondition condition = new UserSelectCondition();
        condition.setQuery("18639760875");
        ResponseVo<PageInfo> responseVo = userService.getUserListByCondition(condition);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }

    @Test
    public void addUser() {
        UserAddRequest request = new UserAddRequest();
        request.setUsername("lvshihao");
        request.setPassword("123456");
        request.setEmail("15945655@qq.com");
        request.setPhone("15978456278");
        request.setRole("1");
        ResponseVo<UserVo> responseVo = userService.addUser(request);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
    @Test
    public void updateUser(){
        Integer userId = 11;
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("lv@163.com");
        ResponseVo responseVo = userService.updateUser(userId, request);
        log.info("list={}", JSON.toJSONString(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}