package com.henu.mall.controller;

import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.pojo.User;
import com.henu.mall.request.AddUser;
import com.henu.mall.service.UserService;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author lv
 * @date 2019-12-15 19:12
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ResponseVo<User> register(@Valid @RequestBody AddUser addUser,
                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult);
        }
        log.info("username {} ",addUser.getUsername());
        User user=new User();
        BeanUtils.copyProperties(addUser,user);
        return userService.register(user);
    }
    @PostMapping("/login")
    public ResponseVo<User> login(User user){
        return userService.login(user);
    }


}
