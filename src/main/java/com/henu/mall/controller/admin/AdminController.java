package com.henu.mall.controller.admin;

import com.github.pagehelper.PageInfo;
import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.manager.AuthManager;
import com.henu.mall.request.AdminLoginRequest;
import com.henu.mall.request.UserAddRequest;
import com.henu.mall.request.UserSelectCondition;
import com.henu.mall.request.UserUpdateRequest;
import com.henu.mall.service.admin.AUserService;
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
 * 管理员接口
 * @author lv
 * @date 2020-03-26 10:10
 */
@Api(description = "后台用户接口服务")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Resource
    private AUserService aUserService;

    @Resource
    private AuthManager authManager;

    @ApiOperation("管理员登录")
    @PostMapping("/user/login")
    public ResponseVo<UserVo> login(@Valid @RequestBody AdminLoginRequest request, HttpSession session){

        ResponseVo<UserVo> login = aUserService.adminLogin(request);
        //设置Session
        session.setAttribute("user", login.getData());
        log.info("/login sessionId={}", session.getId());
        return login;
    }

    @ApiOperation("管理员登出")
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        session.removeAttribute("user");
        authManager.loginOff();
        return ResponseVo.success();
    }

    @ApiOperation("获取当前登录管理员信息")
    @GetMapping("/user")
    @AuthIgnore
    public ResponseVo<UserVo> userInfo(HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        if(user == null){
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        return ResponseVo.success(user);
    }

    @ApiOperation("添加用户")
    @AuthIgnore
    @PostMapping("/user/add")
    public ResponseVo add(@Valid @RequestBody UserAddRequest request){
        return aUserService.addUser(request);
    }

    @ApiOperation("根据用户id获取用户信息")
    @GetMapping("/user/{userId}")
    @AuthIgnore
    public ResponseVo getUserInfo(@PathVariable("userId") Integer userId){
        return aUserService.getUserInfo(userId);
    }

    @ApiOperation("根据用户id更新用户")
    @AuthIgnore
    @PutMapping("/user/{userId}")
    public ResponseVo update(@PathVariable("userId") Integer userId,@RequestBody UserUpdateRequest request){
        return aUserService.updateUser(userId,request);
    }

    @ApiOperation("根据用户id删除用户")
    @AuthIgnore
    @DeleteMapping("/user/{userId}")
    public ResponseVo delete(@PathVariable("userId") Integer userId){
        return  aUserService.delete(userId);
    }

    @ApiOperation("获取用户列表")
    @PostMapping("/user/list")
    @AuthIgnore
    public ResponseVo<PageInfo> list(@Valid @RequestBody UserSelectCondition condition){
        return aUserService.getUserListByCondition(condition);
    }
}
