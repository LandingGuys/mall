package com.henu.mall.mapper;

import com.henu.mall.pojo.User;
import com.henu.mall.request.UserSelectCondition;

import java.util.List;

/**
 * @author lv
 * @date 2020-02-22 21:49
 */
public interface UserExtMapper {

    //分页模糊查询
    List<User> selectUserListByCondition (UserSelectCondition condition);


}
