package com.henu.mall.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@Accessors(chain = true)
public class User {
    private Integer id;

    private String accountId;

    private String username;

    private String token;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String avatarUrl;

    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;


}