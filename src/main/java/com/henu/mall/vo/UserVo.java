package com.henu.mall.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author lv
 * @date 2020-02-21 23:24
 */
@Data
public class UserVo {

    private Integer id;

    private String accountId;

    private String username;

    private String email;

    private String phone;

    private String avatarUrl;

    private Integer role;

    private String password;

    private Date createTime;

    private Date updateTime;

    private String token;
}
