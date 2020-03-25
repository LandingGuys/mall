package com.henu.mall.service;

import com.github.pagehelper.PageInfo;
import com.henu.mall.pojo.User;
import com.henu.mall.request.*;
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
    ResponseVo<UserVo> register(UserRegisterRequest user);
    /**
     * 邮箱、手机号、用户名登录
     */
    ResponseVo<UserVo> login(UserLoginForm userLoginForm);

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
     * 根据用户id 修改信息
     * @param userId
     * @param request
     * @return
     */
    ResponseVo updateMuser(Integer userId,UserUpdateRequest request);

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

    /**
     * 发送邮件并获取验证码
     * @param email
     * @return
     */
    ResponseVo sendEmailAndCheck(String email);

    /**
     * 验证用户是否存在
     * @param userName
     * @return
     */
    ResponseVo checkName(String userName);

    /**
     * 验证邮箱是否已存在
     * @param email
     * @return
     */
    ResponseVo checkEmail(String email);
}
