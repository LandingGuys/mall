package com.henu.mall.service.impl;


import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.mapper.UserMapper;
import com.henu.mall.pojo.User;
import com.henu.mall.pojo.UserExample;
import com.henu.mall.service.UserService;
import com.henu.mall.utils.Md5TokenGenerator;
import com.henu.mall.utils.RedisUtil;
import com.henu.mall.utils.TokeUtil;
import com.henu.mall.vo.ResponseVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.henu.mall.enums.ResponseEnum.GET_USER_INFO_ERROR;
import static com.henu.mall.enums.ResponseEnum.THIRD_PARTY_LOGIN_ERROR;

/**
 * @author lv
 * @date 2019-12-21 17:39
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private Md5TokenGenerator tokenGenerator;

    @Override
    public ResponseVo<User> crateOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
        //token 存入 redis
        //token MD5 加盐
        String token = tokenGenerator.generate(user.getAccountId());
        user.setToken(token);
        TokeUtil.setToken(user.getUsername(),redisUtil,token);

        if(CollectionUtils.isEmpty(dbUsers)){
            //插入
            int i = userMapper.insertSelective(user);
            if(i != 1){
                return ResponseVo.error(THIRD_PARTY_LOGIN_ERROR);
            }
            return getUserInfo(user.getAccountId());
        }
        //更新
        user.setUpdateTime(new Date());
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(user.getAccountId());
        int i = userMapper.updateByExampleSelective(user, example);
        if(i != 1 ){
            return ResponseVo.error(THIRD_PARTY_LOGIN_ERROR);
        }
        return getUserInfo(user.getAccountId());
    }

    @Override
    public ResponseVo login(User user) {
        //判断用户名，密码是否一致
        // TODO 改成邮箱 手机号 token redis
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(user.getUsername())
                .andPasswordEqualTo(DigestUtils.md5DigestAsHex(
                        user.getPassword().getBytes(StandardCharsets.UTF_8)));
        List<User> users = userMapper.selectByExample(example);
        if(users.size() != 0){
            // TODO token redis
//            //token存入redis
//            String token = tokenGenerator.generate(users.get(0).getAccountId());
//            users.get(0).setToken(token);
//            TokeUtil.setToken(users.get(0).getUsername(),redisUtil,token);
            return ResponseVo.success(users.get(0));
        }
        return ResponseVo.error(ResponseEnum.PASSWORD_ERROR);
    }

    /**
     * 邮箱、手机号注册
     *
     * @param user
     * @return
     */
    @Override
    public ResponseVo<User> register(User user) {
        //username不能重复 手机号不能重复
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
        user.setAccountId(UUID.randomUUID().toString());
        //token 存入 redis
        String token= tokenGenerator.generate(user.getAccountId());
        user.setToken(token);
        TokeUtil.setToken(user.getUsername(),redisUtil,token);

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
    public ResponseVo<User> getUserInfo(String accountId) {
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(accountId);

        List<User> users = userMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(users)){
            return ResponseVo.success(users.get(0));
        }
        return ResponseVo.error(GET_USER_INFO_ERROR);
    }
}
