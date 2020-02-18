package com.henu.mall.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lv
 * @date 2020-01-11 19:49
 * NotBank 用于String 判断空格空
 * NotEmpty 用于集合空
 * NotNull 判断是否为NULL
 */
@Data
public class UserRegisterRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    private String email;
}
