package com.henu.mall.service;

import com.henu.mall.pojo.User;
import com.henu.mall.vo.ResponseVo;

/**
 * @author lv
 * @date 2019-12-21 17:38
 */
public interface UserService {
    /**
     * 第三方注册登录
     */
    ResponseVo<User> crateOrUpdate(User user);

    /**
     * 邮箱、手机号注册
     * @param user
     * @return
     */
    ResponseVo<User> register(User user);
    /**
     * 邮箱、手机号登录
     */
   ResponseVo<User> login(User user);

    /**
     * 获取用户信息
     */
    ResponseVo<User> getUserInfo(String accountId);
}
