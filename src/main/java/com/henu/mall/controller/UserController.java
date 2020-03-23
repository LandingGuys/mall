package com.henu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.manager.AuthManager;
import com.henu.mall.request.*;
import com.henu.mall.service.UserService;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
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
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AuthManager authManager;

    @PostMapping("/user/register")
    public ResponseVo<UserVo> register(@Valid @RequestBody UserRegisterRequest addUser){
        //log.info("username {} ",addUser.getUsername());

        return userService.register(addUser);
    }
    @PostMapping("/user/login")
    public ResponseVo<UserVo> login(@Valid @RequestBody UserLoginForm userLoginForm,  HttpSession session){
        // TODO 改成邮箱 手机号 token cookie

        ResponseVo<UserVo> login = userService.login(userLoginForm);
        //设置Session
        session.setAttribute("user", login.getData());
        log.info("/login sessionId={}", session.getId());
        return login;
    }

    @GetMapping("/user")
    @AuthIgnore
    public ResponseVo<UserVo> userInfo(HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        if(user == null){
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        return ResponseVo.success(user);
    }
    @PutMapping("/user")
    @AuthIgnore
    public ResponseVo<UserVo> updateUser(@RequestBody UserUpdateRequest request,HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        ResponseVo responseVo = userService.updateUserImage(user.getId(), request);
        session.setAttribute("user", responseVo.getData());
        return responseVo;
    }
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        session.removeAttribute("user");
        authManager.loginOff();
        return ResponseVo.success();
    }
    @PostMapping("/user/list")
    @AuthIgnore
    public ResponseVo<PageInfo> list(@Valid @RequestBody UserSelectCondition condition){
        return userService.getUserListByCondition(condition);
    }
    @AuthIgnore
    @PostMapping("/user/add")
    public ResponseVo add(@Valid @RequestBody UserAddRequest request){
       return userService.addUser(request);
    }
    @GetMapping("/user/{userId}")
    @AuthIgnore
    public ResponseVo getUserInfo(@PathVariable("userId") Integer userId){
       return userService.getUserInfo(userId);
    }
    @AuthIgnore
    @PutMapping("/user/{userId}")
    public ResponseVo update(@PathVariable("userId") Integer userId,@RequestBody UserUpdateRequest request){
        return userService.updateUser(userId,request);
    }
    @AuthIgnore
    @DeleteMapping("/user/{userId}")
    public ResponseVo delete(@PathVariable("userId") Integer userId){
        return  userService.delete(userId);
    }

    @GetMapping("/user/email")
    public ResponseVo email(@RequestParam("email") String email){
        return userService.validateEmail(email);
    }

    @GetMapping("/user/checkName")
    public ResponseVo checkName(@RequestParam("userName") String userName){
        return userService.checkName(userName);
    }

}
