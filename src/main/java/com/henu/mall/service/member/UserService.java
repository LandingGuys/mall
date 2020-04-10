package com.henu.mall.service.member;

import com.henu.mall.pojo.User;
import com.henu.mall.request.UserLoginForm;
import com.henu.mall.request.UserRegisterRequest;
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
     * 根据用户id 修改信息
     * @param userId
     * @param request
     * @return
     */
    ResponseVo updateMuser(Integer userId,UserUpdateRequest request);

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

    /**
     * 手机号验证
     * @param phone
     * @return
     */
    ResponseVo validatePhone(String phone);
}
