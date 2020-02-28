package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-02-23 19:09
 */
@Data
public class UserUpdateRequest {

    private  Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String avatarUrl;

    private String role;
}
