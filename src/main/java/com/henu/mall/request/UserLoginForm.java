package com.henu.mall.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lv
 * @date 2020-02-20 16:00
 */
@Data
public class UserLoginForm {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
