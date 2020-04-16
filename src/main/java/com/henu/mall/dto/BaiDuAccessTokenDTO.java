package com.henu.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lvbenwei11319
 * @date  2020/1/16 15:45
 * @desc 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Component
public class BaiDuAccessTokenDTO {

    private String grant_type= "authorization_code";

    @Value("${BaiDu_client_id}")
    private String client_id;

    private String code;

    @Value("${BaiDu_client_secret}")
    private String client_secret;

    @Value("${BaiDu_Redirect_uri}")
    private String redirect_uri;
}