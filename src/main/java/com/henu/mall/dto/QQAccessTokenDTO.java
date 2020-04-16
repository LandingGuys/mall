package com.henu.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lvbenwei11319
 * @date  2020/1/10 19:40
 * @desc 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Component
public class QQAccessTokenDTO {

    private String grant_type= "authorization_code";

    @Value("${QQ_client_id}")
    private String client_id;

    private String code;

    @Value("${QQ_client_secret}")
    private String client_secret;

    @Value("${QQ_Redirect_uri}")
    private String redirect_uri;
}