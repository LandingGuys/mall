package com.henu.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lvbenwei11319
 * @date  2020/1/10 19:40
 * @desc 
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class QQAccessTokenDTO {
    private String grant_type;
    private String client_id;
    private String code;
    private String client_secret;
    private String redirect_uri;
}