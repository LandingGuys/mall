package com.henu.mall.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.consts.MallConsts;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.manager.AuthManager;
import com.henu.mall.mapper.UserExtMapper;
import com.henu.mall.mapper.UserMapper;
import com.henu.mall.pojo.User;
import com.henu.mall.pojo.UserExample;
import com.henu.mall.request.*;
import com.henu.mall.service.UserService;
import com.henu.mall.utils.CheckUtil;
import com.henu.mall.utils.RedisUtil;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.henu.mall.enums.ResponseEnum.GET_USER_INFO_ERROR;
import static com.henu.mall.enums.ResponseEnum.THIRD_PARTY_LOGIN_ERROR;

/**
 * @author lv
 * @date 2019-12-21 17:39
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthManager authManager;

    @Resource
    private UserExtMapper userExtMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    JavaMailSenderImpl javaMailSender;

    @Override
    public ResponseVo<UserVo> crateOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(userExample);
        UserVo userVo = new UserVo();
        if(CollectionUtils.isEmpty(dbUsers)){
            //插入
            int i = userMapper.insertSelective(user);
            if(i != 1){
                return ResponseVo.error(THIRD_PARTY_LOGIN_ERROR);
            }
            return getUserInfo(user.getAccountId(),userVo);
        }
        //更新
        User updateUser = new User();
        updateUser.setAccountId(user.getAccountId());
        updateUser.setUpdateTime(new Date());
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(updateUser.getAccountId());
        int i = userMapper.updateByExampleSelective(updateUser, example);
        if(i != 1 ){
            return ResponseVo.error(THIRD_PARTY_LOGIN_ERROR);
        }
        return getUserInfo(user.getAccountId(),userVo);
    }

    /**
     * 用户名、邮箱、手机号登录
     * @param userLoginForm
     * @return
     */
    @Override
    public ResponseVo login(UserLoginForm userLoginForm) {
        //判断用户名、邮箱、手机号，密码是否一致
        String usernameOrEmailOrPhone = userLoginForm.getUsernameOrEmailOrPhone();
        String password = userLoginForm.getPassword();
        List<User> users;
        if(CheckUtil.isEmail(usernameOrEmailOrPhone)){
            //邮箱
            UserExample exampleByEmail = new UserExample();
            exampleByEmail.createCriteria().andEmailEqualTo(usernameOrEmailOrPhone)
                    .andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));

             users = userMapper.selectByExample(exampleByEmail);
        }else if(CheckUtil.isMobile(usernameOrEmailOrPhone)){
            //手机号
            UserExample exampleByPhone = new UserExample();
            exampleByPhone.createCriteria().andPhoneEqualTo(usernameOrEmailOrPhone)
                    .andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
             users = userMapper.selectByExample(exampleByPhone);
        }else{
            //用户名
            UserExample exampleByUserName = new UserExample();
            exampleByUserName.createCriteria().andUsernameEqualTo(usernameOrEmailOrPhone)
                    .andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
            users = userMapper.selectByExample(exampleByUserName);
        }
        UserVo userVo = new UserVo();
        if(users.size() != 0){
            BeanUtils.copyProperties(users.get(0),userVo);
            //设置token
            userVo.setToken(authManager.login(userVo));
            return ResponseVo.success(userVo);
        }
        return ResponseVo.error(ResponseEnum.PASSWORD_ERROR);
    }
    /**
     * 邮箱、手机号注册
     * @param addUser
     * @return
     */
    @Override
    public ResponseVo<UserVo> register(UserRegisterRequest addUser) {
        User user = new User();
        BeanUtils.copyProperties(addUser,user);
        //1.先判断是邮箱还是手机号 再校验验证码 从redis 获取验证码 对比
        String phoneOrEmail = addUser.getPhoneOrEmail();
        if(phoneOrEmail.contains("@")){
            //邮箱
            if(!CheckUtil.isEmail(phoneOrEmail)){
                return ResponseVo.error(ResponseEnum.PHONE_OR_EMAIL_ERROR);
            }
            String emailKey = String.format(MallConsts.EMAIL_KEY_TEMPLATE,phoneOrEmail);
            getCodeFormRedis(emailKey,addUser.getVerifyCode());
            user.setEmail(phoneOrEmail);
        } else{
            //手机号
            if(!CheckUtil.isMobile(phoneOrEmail)){
                return ResponseVo.error(ResponseEnum.PHONE_OR_EMAIL_ERROR);
            }
            String phoneKey = String.format(MallConsts.PHONE_KEY_TEMPLATE,phoneOrEmail);
            getCodeFormRedis(phoneKey,addUser.getVerifyCode());
            user.setPhone(phoneOrEmail);
        }
        user.setRole(RoleEnum.CUSTOMER.getCode());
        user.setAccountId(UUID.randomUUID().toString());
        user.setPassword(DigestUtils.md5DigestAsHex(
                user.getPassword().getBytes(StandardCharsets.UTF_8)
        ));
        user.setAvatarUrl("http://shuixin.oss-cn-beijing.aliyuncs.com/moren.jpg");
        int resultCount = userMapper.insertSelective(user);
        if(resultCount <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }
    private void getCodeFormRedis(String key,String code){
        if(!redisUtil.hasKey(key)){
            throw new RuntimeException(ResponseEnum.VERIFY_CODE_REDIS_NOT_EXIST.getDesc());
        }
        String verifyCodeFormRedis = redisUtil.get(key);
        if(!verifyCodeFormRedis.equals(code)){
            throw new RuntimeException(ResponseEnum.VERIFY_CODE_REDIS_NOT_EXIST.getDesc());
        }
        redisUtil.delete(key);
    }

    private ResponseVo<UserVo> getUserInfo(String accountId,UserVo userVo) {
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(accountId);

        List<User> users = userMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(users)){
            BeanUtils.copyProperties(users.get(0),userVo);
            userVo.setToken(authManager.login(userVo));
            return ResponseVo.success(userVo);
        }
        return ResponseVo.error(GET_USER_INFO_ERROR);
    }

    /**
     * 获取用户列表
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
     * 根据详细信息添加用户
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

    private Long validateUserName(User user) {
        // username 不能重复
        UserExample userExampleByUsername = new UserExample();
        userExampleByUsername.createCriteria().andUsernameEqualTo(user.getUsername());
        long countByUsername = userMapper.countByExample(userExampleByUsername);
        return countByUsername;
    }
    private Long validateEmail(User user){
        //email 不能重复
        UserExample userExampleByEmail= new UserExample();
        userExampleByEmail.createCriteria().andEmailEqualTo(user.getEmail());
        long countByEmail = userMapper.countByExample(userExampleByEmail);
        return countByEmail;
    }

    @Override
    public ResponseVo updateUser(Integer userId, UserUpdateRequest request) {
        User userSelect = userMapper.selectByPrimaryKey(userId);
        if(userSelect == null){
            return ResponseVo.error(ResponseEnum.USER_NOT_EXIST);
        }
        User user =new User();
//        BeanUtils.copyProperties(userSelect,user);
        BeanUtils.copyProperties(request,user);
        // 修改用户前username,email的验证 （验证除自己username,email 之外是否还有 已存在的）
        if(!user.getUsername().equals(userSelect.getUsername()) ){
            Long hasUserName = validateUserName(user);
            if(hasUserName > 0){
                return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
            }
        }
        if(!user.getEmail().equals(userSelect.getEmail())){
            Long hasEmail = validateEmail(user);
            if(hasEmail > 0){
                return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
            }
        }
        user.setRole(Integer.valueOf(request.getRole()));
        int row = userMapper.updateByPrimaryKeySelective(user);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.UPDATE_USER_ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<UserVo> updateUserImage(Integer userId, UserUpdateRequest request) {
        User userSelect = userMapper.selectByPrimaryKey(userId);
        if(userSelect == null){
            return ResponseVo.error(ResponseEnum.USER_NOT_EXIST);
        }
        User user = new User();
        user.setAvatarUrl(request.getAvatarUrl());
        user.setId(userId);
        int row = userMapper.updateByPrimaryKeySelective(user);
        if(row <= 0){
           return ResponseVo.error(ResponseEnum.UPLOAD_USER_ERROR);
        }
        User userDb = userMapper.selectByPrimaryKey(userId);
        UserVo userVo =new UserVo();
        BeanUtils.copyProperties(userDb,userVo);
        return ResponseVo.success(userVo);
    }

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

    @Override
    public ResponseVo validateEmail(String email) {
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(email);
        List<User> userList = userMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(userList)){
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }
        String result=String.valueOf((int)((Math.random()*9+1)*100000));
        redisUtil.setEx(String.format(MallConsts.EMAIL_KEY_TEMPLATE,email),result,15,TimeUnit.MINUTES);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("欢迎注册智慧药房，离完成就差一步了");
            helper.setText("亲爱的用户：您好！感谢您使用智慧药房服务。" +
                    "您正在进行邮箱验证，请在验证码输入框中输入此次验证码："+"<b style='color:red'>"+result+"</b>"+
                    " 请在15分钟内按页面提示提交验证码以完成验证，切勿将验证码泄露于他人。" +
                    "如非本人操作，请忽略此邮件，由此给您带来的不便请您谅解！",true);
            helper.setTo(email);
            helper.setFrom("community@wast.club");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        log.info("邮箱发送成功，验证码为"+result);
        return ResponseVo.success();
    }

    @Override
    public ResponseVo checkName(String userName) {
        // username 不能重复
        UserExample userExampleByUsername = new UserExample();
        userExampleByUsername.createCriteria().andUsernameEqualTo(userName);
        long countByUsername = userMapper.countByExample(userExampleByUsername);
        if(countByUsername >= 1){
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }
       return ResponseVo.success();
    }
}
