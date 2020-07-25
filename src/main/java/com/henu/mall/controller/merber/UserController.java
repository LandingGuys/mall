package com.henu.mall.controller.merber;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.manager.AuthManager;
import com.henu.mall.request.UserLoginForm;
import com.henu.mall.request.UserRegisterRequest;
import com.henu.mall.request.UserUpdateRequest;
import com.henu.mall.service.member.UserService;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author lv
 * @date 2019-12-15 19:12
 */
@Slf4j
@RestController
@Api(description = "前台用户服务接口")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AuthManager authManager;

    @ApiOperation("用户注册")
    @PostMapping("/user/register")
    public ResponseVo<UserVo> register(@Valid @RequestBody UserRegisterRequest addUser){
        //log.info("username {} ",addUser.getUsername());

        return userService.register(addUser);
    }

    @ApiOperation("用户登录")
    @PostMapping("/user/login")
    public ResponseVo<UserVo> login(@Valid @RequestBody UserLoginForm userLoginForm,  HttpSession session){

        ResponseVo<UserVo> login = userService.login(userLoginForm);
        //设置Session
        session.setAttribute("user", login.getData());
        log.info("/login sessionId={}", session.getId());
        return login;
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/user")
    @AuthIgnore
    public ResponseVo<UserVo> userInfo(HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        if(user == null){
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        return ResponseVo.success(user);
    }

    @ApiOperation("更新用户")
    @PutMapping("/user")
    @AuthIgnore
    public ResponseVo<UserVo> updateUser(@RequestBody UserUpdateRequest request,HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        ResponseVo responseVo = userService.updateMuser(user.getId(), request);
        session.setAttribute("user", responseVo.getData());
        return responseVo;
    }

    @ApiOperation("登出")
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        session.removeAttribute("user");
        authManager.loginOff();
        return ResponseVo.success();
    }
    /**
     * 注册时邮箱验证
     * @param email
     * @return
     */
    @ApiOperation("注册时邮箱验证")
    @GetMapping("/user/email")
    public ResponseVo email(@RequestParam("email") String email){
        return userService.validateEmail(email);
    }

    @ApiOperation("注册时手机号验证")
    @GetMapping("/user/phone")
    public ResponseVo phone(@RequestParam("phone") String phone){
        return userService.validatePhone(phone);
    }
    /**
     * 修改个人邮箱时发送邮箱
     * @param email
     * @return
     */
    @ApiOperation("发送邮件")
    @GetMapping("/user/sendEmail")
    public ResponseVo sendEmail(@RequestParam("email") String email){
        return userService.sendEmailAndCheck(email);
    }

    @ApiOperation("检查用户名")
    @GetMapping("/user/checkName")
    public ResponseVo checkName(@RequestParam("userName") String userName){
        return userService.checkName(userName);
    }

    @ApiOperation("检查邮箱")
    @GetMapping("/user/checkEmail")
    public ResponseVo checkEmail(@RequestParam("email") String email){
        return userService.checkEmail(email);
    }

}
