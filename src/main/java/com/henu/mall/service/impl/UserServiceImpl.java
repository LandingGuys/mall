package com.henu.mall.service.impl;

import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.mapper.UserMapper;
import com.henu.mall.pojo.User;
import com.henu.mall.pojo.UserExample;
import com.henu.mall.service.UserService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author lv
 * @date 2019-12-21 17:39
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseVo<User> register(User user) {
        //username不能重复
        UserExample userExampleByUsername = new UserExample();
        userExampleByUsername.createCriteria().andUsernameEqualTo(user.getUsername());
        long countByUsername = userMapper.countByExample(userExampleByUsername);
        if(countByUsername>0){
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }
        //email 不能重复
        UserExample userExampleByEmail= new UserExample();
        userExampleByEmail.createCriteria().andEmailEqualTo(user.getEmail());
        long countByEmail = userMapper.countByExample(userExampleByEmail);
        if(countByEmail>0){
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }

        user.setRole(RoleEnum.CUSTOMER.getCode());
        user.setPassword(DigestUtils.md5DigestAsHex(
                user.getPassword().getBytes(StandardCharsets.UTF_8)
        ));
        int resultCount = userMapper.insertSelective(user);
        if(resultCount==0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<User> login(String string, String password) {
        return null;
    }
}
