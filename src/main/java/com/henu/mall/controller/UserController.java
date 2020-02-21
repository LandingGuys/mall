package com.henu.mall.controller;

import com.henu.mall.pojo.User;
import com.henu.mall.request.UserLoginForm;
import com.henu.mall.request.UserRegisterRequest;
import com.henu.mall.service.UserService;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterRequest addUser){
        log.info("username {} ",addUser.getUsername());
        User user=new User();
        BeanUtils.copyProperties(addUser,user);
        return userService.register(user);
    }
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,  HttpSession session){
        // TODO 改成邮箱 手机号 token cookie
        User user = new User();
        BeanUtils.copyProperties(userLoginForm,user);
        ResponseVo<User> login = userService.login(user);
        //设置Session
        session.setAttribute("user", login.getData());
        log.info("/login sessionId={}", session.getId());
        //response.addCookie(new Cookie("token",login.getData().getToken()));
        return login;
    }

    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        User user =(User) session.getAttribute("user");
        return ResponseVo.success(user);
    }
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        session.removeAttribute("user");
        return ResponseVo.success();
    }

}
