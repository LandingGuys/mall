package com.henu.mall.service.impl;

import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.mapper.UserMapper;
import com.henu.mall.pojo.User;
import com.henu.mall.pojo.UserExample;
import com.henu.mall.service.UserService;
import com.henu.mall.vo.ResponseVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * @author lv
 * @date 2019-12-21 17:39
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public Boolean crateOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(dbUsers)){
            //插入
            int i = userMapper.insertSelective(user);
            if(i ==1){
                return true;
            }else {
                return false;
            }
        }
        //更新
        User dbUser = dbUsers.get(0);
        User updateUser =new User();
        updateUser.setToken(user.getToken())
                .setRole(user.getRole())
                .setUsername(user.getUsername())
                .setAccountId(user.getAccountId())
                .setUpdateTime(new Date());
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(dbUser.getId());
        int i = userMapper.updateByExampleSelective(updateUser, example);
        if(i == 1 ){
            return true;
        }else {
           return false;
        }
    }

    @Override
    public ResponseVo<User> login(String string, String password) {

        return null;
    }

    /**
     * 邮箱、手机号注册
     *
     * @param user
     * @return
     */
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
}
