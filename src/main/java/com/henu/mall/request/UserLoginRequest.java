package com.henu.mall.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lv
 * @date 2020-01-28 10:02
 */
@Data
public class UserLoginRequest {
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;


}
