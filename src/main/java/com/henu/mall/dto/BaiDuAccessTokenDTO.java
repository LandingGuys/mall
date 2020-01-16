package com.henu.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lvbenwei11319
 * @date  2020/1/16 15:45
 * @desc 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BaiDuAccessTokenDTO {
    private String grant_type;
    private String client_id;
    private String code;
    private String client_secret;
    private String redirect_uri;
}