package com.henu.mall.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lv
 * @date 2020-02-23 19:00
 */
@Data
public class UserAddRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "角色不能为空")
    private String role;
}
