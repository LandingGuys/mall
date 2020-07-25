package com.henu.mall.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lv
 * @date 2020-04-16 10:53
 */
@Data
@Component
public class WeiBoAccessTokenDTO {

    @Value("${WeiBo_client_id}")
    private String clientId;

    @Value("${WeiBo_client_secret}")
    private String clientSecret;

    private String code;

    private String grant_type = "authorization_code";

    @Value("${WeiBo_Redirect_uri}")
    private String redirectUri;
}
