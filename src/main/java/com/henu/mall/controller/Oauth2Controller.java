package com.henu.mall.controller;

import com.henu.mall.dto.QQAccessTokenDTO;
import com.henu.mall.dto.QQUser;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.pojo.User;
import com.henu.mall.provider.QQProvider;
import com.henu.mall.service.UserService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author lvbenwei11319
 * @date  2020/1/10 19:28
 * @desc 第三方服务登录
 */
@RestController
public class Oauth2Controller {

    @Value("${QQ_client_id}")
    private String QQClientId;
    @Value("${QQ_client_secret}")
    private String QQClientSecret;
    @Value("${QQ_Redirect_uri}")
    private String QQRedirectUri;


    @Resource
    private UserService userService;

    @GetMapping("/qqcallback")
    public ResponseVo qqCallback(@RequestParam("code") String code,
                                         HttpServletResponse response){
        QQAccessTokenDTO qqAccessTokenDTO = new QQAccessTokenDTO("authorization_code", QQClientId, code, QQClientSecret, QQRedirectUri);
        String accessToken= QQProvider.getAccessToken(qqAccessTokenDTO);
        String openId=QQProvider.getOpenId(accessToken);
        QQUser qqUser=QQProvider.getUserInfo(openId,QQClientId,accessToken);
        User user = new User();
        user.setAccountId(openId)
                .setUsername(qqUser.getNickname())
                .setToken(UUID.randomUUID().toString())
                .setRole(RoleEnum.CUSTOMER.getCode());
        Boolean crateOrUpdate = userService.crateOrUpdate(user);
        if(crateOrUpdate){
            return ResponseVo.success();
        }else{
            return ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_QQ_ERROR);
        }

    }
    @PostMapping("/weiBo")
    public ResponseVo<Object> weiBoCallback(){
        return null;
    }
    @PostMapping("/weiXin")
    public ResponseVo<Object> weiXinCallBack(){
        return null;
    }
}