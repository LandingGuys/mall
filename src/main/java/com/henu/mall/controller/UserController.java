package com.henu.mall.controller;

import com.henu.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lv
 * @date 2019-12-15 19:12
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @ResponseBody
    @RequestMapping("/register")
    public ResponseVo<Object> register(){

        return null;
    }
}
