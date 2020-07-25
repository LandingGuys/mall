package com.henu.mall.service.admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.manager.AuthManager;
import com.henu.mall.mapper.UserExtMapper;
import com.henu.mall.mapper.UserMapper;
import com.henu.mall.pojo.User;
import com.henu.mall.pojo.UserExample;
import com.henu.mall.request.AdminLoginRequest;
import com.henu.mall.request.UserAddRequest;
import com.henu.mall.request.UserSelectCondition;
import com.henu.mall.request.UserUpdateRequest;
import com.henu.mall.service.admin.AUserService;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author lv
 * @date 2020-03-26 10:23
 */
@Service
public class AUserServiceImpl implements AUserService {

    @Resource
    private UserExtMapper userExtMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthManager authManager;

    @Override
    public ResponseVo adminLogin(AdminLoginRequest request) {
        //用户名
        UserExample exampleByUserName = new UserExample();
        exampleByUserName.createCriteria().andUsernameEqualTo(request.getName())
                .andPasswordEqualTo(DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8)));
        List<User> users = userMapper.selectByExample(exampleByUserName);

        UserVo userVo = new UserVo();
        if(users.size() != 0){
            User user = users.get(0);

            if(!user.getRole().equals(RoleEnum.ADMIN.getCode())){
                return ResponseVo.error(ResponseEnum.USER_PERMISSION_ERROR);
            }
            BeanUtils.copyProperties(user,userVo);
            //设置token
            userVo.setToken(authManager.login(userVo));
            return ResponseVo.success(userVo);
        }
        return ResponseVo.error(ResponseEnum.PASSWORD_ERROR);
    }

    /**
     * 获取用户列表 后台
     *
     * @param condition
     * @param
     * @param
     * @return
     */
    @Override
    public ResponseVo<PageInfo> getUserListByCondition(UserSelectCondition condition) {

        PageHelper.startPage(condition.getPageNum(),condition.getPageSize());
        List<User> userList = userExtMapper.selectUserListByCondition(condition);

        List<UserVo> userVoList = userList.stream().map(e -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(e, userVo);
            return userVo;
        }).collect(Collectors.toList());

        PageInfo  pageInfo = new PageInfo<>(userList);
        pageInfo.setList(userVoList);


        return ResponseVo.success(pageInfo);
    }

    /**
     * 根据详细信息添加用户 后台
     *
     * @param request
     * @return
     */
    @Override
    public ResponseVo addUser(UserAddRequest request) {

        User user = new User();
        BeanUtils.copyProperties(request,user);
        //username不能重复 手机号不能重复
        Long hasUserName = validateUserName(user);
        if(hasUserName > 0){
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }
        Long hasEmail = validateEmail(user);
        if(hasEmail > 0){
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }
        user.setRole(Integer.valueOf(request.getRole()));
        user.setAccountId(UUID.randomUUID().toString());
        user.setPassword(DigestUtils.md5DigestAsHex(
                user.getPassword().getBytes(StandardCharsets.UTF_8)
        ));
        user.setAvatarUrl("http://shuixin.oss-cn-beijing.aliyuncs.com/moren.jpg");
        int row = userMapper.insertSelective(user);
        if(row <=0){
            return ResponseVo.error(ResponseEnum.ADD_USER_ERROR);
        }
        return ResponseVo.success();
    }
    /**
     * 修改用户个人信息 后台
     * @param userId
     * @param request
     * @return
     */
    @Override
    public ResponseVo updateUser(Integer userId, UserUpdateRequest request) {
        User userSelect = userMapper.selectByPrimaryKey(userId);
        if(userSelect == null){
            return ResponseVo.error(ResponseEnum.USER_NOT_EXIST);
        }
        User user =new User();
        BeanUtils.copyProperties(request,user);
        user.setId(userId);
        // 修改用户前username,email的验证 （验证除自己username,email 之外是否还有 已存在的）
        if(request.getUsername() !=null){
            if(!user.getUsername().equals(userSelect.getUsername()) ){
                Long hasUserName = validateUserName(user);
                if(hasUserName > 0){
                    return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
                }
            }
        }
        if(request.getEmail() != null){
            if(!user.getEmail().equals(userSelect.getEmail())){
                Long hasEmail = validateEmail(user);
                if(hasEmail > 0){
                    return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
                }
            }
        }
        if(request.getRole()!=null){
            user.setRole(Integer.valueOf(request.getRole()));
        }

        int row = userMapper.updateByPrimaryKeySelective(user);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.UPDATE_USER_ERROR);
        }
        User userV = userMapper.selectByPrimaryKey(userId);
        UserVo userVo =new UserVo();
        BeanUtils.copyProperties(userV,userVo);

        return ResponseVo.success(userVo);
    }

    /**
     * 获取用户 后台
     * @param userId
     * @return
     */
    @Override
    public ResponseVo<UserVo> getUserInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ResponseVo.error(ResponseEnum.USER_NOT_EXIST);
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return ResponseVo.success(userVo);
    }

    /**
     * 删除用户 后台
     * @param userId
     * @return
     */
    @Override
    public ResponseVo delete(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user ==null){
            return ResponseVo.error(ResponseEnum.USER_NOT_EXIST);
        }
        int row = userMapper.deleteByPrimaryKey(userId);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.USER_DELETE_ERROR);
        }
        return ResponseVo.success();
    }
    /**
     * 验证用户名不能重复
     * @param user
     * @return
     */
    private Long validateUserName(User user) {
        // username 不能重复
        UserExample userExampleByUsername = new UserExample();
        userExampleByUsername.createCriteria().andUsernameEqualTo(user.getUsername());
        long countByUsername = userMapper.countByExample(userExampleByUsername);
        return countByUsername;
    }

    /**
     * 验证邮箱不能重复
     * @param user
     * @return
     */
    private Long validateEmail(User user){
        //email 不能重复
        UserExample userExampleByEmail= new UserExample();
        userExampleByEmail.createCriteria().andEmailEqualTo(user.getEmail());
        long countByEmail = userMapper.countByExample(userExampleByEmail);
        return countByEmail;
    }
}
