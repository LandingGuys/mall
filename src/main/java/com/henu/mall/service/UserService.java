package com.henu.mall.service;

import com.github.pagehelper.PageInfo;
import com.henu.mall.pojo.User;
import com.henu.mall.request.UserAddRequest;
import com.henu.mall.request.UserSelectCondition;
import com.henu.mall.request.UserUpdateRequest;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;

/**
 * @author lv
 * @date 2019-12-21 17:38
 */
public interface UserService {
    /**
     * 第三方注册登录
     */
    ResponseVo<UserVo> crateOrUpdate(User user);

    /**
     * 邮箱、手机号注册
     * @param user
     * @return
     */
    ResponseVo<UserVo> register(User user);
    /**
     * 邮箱、手机号登录
     */
   ResponseVo<UserVo> login(User user);

//    /**
//     * 获取用户信息
//     */
//    ResponseVo<User> getUserInfo(String accountId);

    /**
     * 根据查询条件获取用户列表
     * @param condition
     * @param
     * @param
     * @return
     */
    ResponseVo<PageInfo> getUserListByCondition(UserSelectCondition condition);

    /**
     *根据详细信息添加用户
     * @param request
     * @return
     */
    ResponseVo<UserVo> addUser(UserAddRequest request);

    /**
     * 根据用户id 修改信息 username email 必传
     * @param userId
     * @return
     */
    ResponseVo updateUser(Integer userId, UserUpdateRequest request);

    /**
     * 根据用户id 修改头像
     * @param userId
     * @param request
     * @return
     */
    ResponseVo updateUserImage(Integer userId,UserUpdateRequest request);

    /**
     * 根据用户id 获取用户信息
     * @param userId
     * @return
     */
    ResponseVo<UserVo> getUserInfo(Integer userId);

    /**
     * 根据用户id 删除用户
     * @param userId
     * @return
     */
    ResponseVo delete(Integer userId);

    /**
     * 邮箱验证
     * @param email
     */
    ResponseVo validateEmail(String email);
}
