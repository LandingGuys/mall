package com.henu.mall.service;

import com.henu.mall.pojo.User;
import com.henu.mall.vo.ResponseVo;

/**
 * @author lv
 * @date 2019-12-21 17:38
 */
public interface UserService {
    /**
     * 用户注册
     */
        ResponseVo<User> register(User user);
    /**
     * 登录
     */
   ResponseVo<User> login(String string,String password);
}
