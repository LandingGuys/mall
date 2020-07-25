package com.henu.mall.service.admin;

import com.github.pagehelper.PageInfo;
import com.henu.mall.request.AdminLoginRequest;
import com.henu.mall.request.UserAddRequest;
import com.henu.mall.request.UserSelectCondition;
import com.henu.mall.request.UserUpdateRequest;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;

/**
 * 管理员用户操作
 * @author lv
 * @date 2020-03-26 10:14
 */
public interface AUserService {
    /**
     * 管理员后台登录
     * @param request
     * @return
     */
    ResponseVo adminLogin(AdminLoginRequest request);
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
}
