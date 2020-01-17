package com.henu.mall.controller;

import com.henu.mall.dto.BaiDuAccessTokenDTO;
import com.henu.mall.dto.BaiDuUser;
import com.henu.mall.dto.QQAccessTokenDTO;
import com.henu.mall.dto.QQUser;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.pojo.User;
import com.henu.mall.provider.BaiDuProvider;
import com.henu.mall.provider.QQProvider;
import com.henu.mall.service.UserService;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author lvbenwei11319
 * @date  2020/1/10 19:28
 * @desc 第三方服务登录
 */
@Slf4j
@RestController
public class Oauth2Controller {

    @Value("${QQ_client_id}")
    private String QQClientId;
    @Value("${QQ_client_secret}")
    private String QQClientSecret;
    @Value("${QQ_Redirect_uri}")
    private String QQRedirectUri;

    @Value("${BaiDu_client_id}")
    private String BaiDuClientId;
    @Value("${BaiDu_client_secret}")
    private String BaiDuClientSecret;
    @Value("${BaiDu_Redirect_uri}")
    private String BaiDuRedirectUri;


    @Resource
    private UserService userService;
    @GetMapping("/oauth/{req}")
    public void oauth(@PathVariable("req") String req, HttpServletResponse response) throws IOException{
        if("qq".equals(req)){
            QQAccessTokenDTO qqAccessTokenDTO = new QQAccessTokenDTO();
            qqAccessTokenDTO.setClient_id(QQClientId)
                    .setRedirect_uri(QQRedirectUri);
            String requestQqOauthUrl = QQProvider.requestOauthUrl(qqAccessTokenDTO);
            //重定向
            response.sendRedirect(requestQqOauthUrl);
        }else if ("baidu".equals(req)){
            BaiDuAccessTokenDTO baiDuAccessTokenDTO = new BaiDuAccessTokenDTO();
            baiDuAccessTokenDTO.setClient_id(BaiDuClientId)
                    .setRedirect_uri(BaiDuRedirectUri);
            String requestBaiDuOauthUrl = BaiDuProvider.requestOauthUrl(baiDuAccessTokenDTO);
            //重定向
            response.sendRedirect(requestBaiDuOauthUrl);
        }else if("weixin".equals(req)){

        }else{

        }

    }
    @GetMapping("/qqcallback")
    public ResponseVo qqCallback(@RequestParam("code") String code){
        QQAccessTokenDTO qqAccessTokenDTO = new QQAccessTokenDTO("authorization_code", QQClientId, code, QQClientSecret, QQRedirectUri);
        String accessToken= QQProvider.getAccessToken(qqAccessTokenDTO);
        String openId=QQProvider.getOpenId(accessToken);
        QQUser qqUser=QQProvider.getUserInfo(openId,QQClientId,accessToken);
        User user = new User();
        user.setAccountId(openId)
                .setToken(UUID.randomUUID().toString())
                .setRole(RoleEnum.CUSTOMER.getCode());
        if(qqUser.getNickname()==null){
            user.setUsername("qq"+openId);
        }else{
            user.setUsername(qqUser.getNickname());
        }
        if(StringUtils.isNoneBlank(qqUser.getFigureurl_qq_1())){
            user.setAvatarUrl(qqUser.getFigureurl_qq_1());
        }else{
            user.setAvatarUrl("https://shuixin.oss-cn-beijing.aliyuncs.com/tian.png");
        }
        Boolean crateOrUpdate = userService.crateOrUpdate(user);
        if(!crateOrUpdate){
            return ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_QQ_ERROR);
        }
        return ResponseVo.success();
    }
    @PostMapping("/weiBoCallBack")
    public ResponseVo weiBoCallback(){
        return null;
    }
    @GetMapping("/baiducallback")
    public ResponseVo baiDuCallBack(@RequestParam("code") String code){
        BaiDuAccessTokenDTO baiDuAccessTokenDTO = new BaiDuAccessTokenDTO("authorization_code", BaiDuClientId, code, BaiDuClientSecret, BaiDuRedirectUri);
        String accessToken = BaiDuProvider.getAccessToken(baiDuAccessTokenDTO);

        BaiDuUser baiduUser=BaiDuProvider.getUser(accessToken);
        if(baiduUser!=null && baiduUser.getUserid()!=null){
            //登录成功,写cookie和session/改为redis
            String token=UUID.randomUUID().toString();
//            Integer expire=7200;
//            redisTemplate.opsForValue().set(String.format("token_%s",token),String.valueOf(githubUser.getId()),expire, TimeUnit.SECONDS);
            User user=new User();
            user.setAccountId(baiduUser.getUserid())
                    .setRole(RoleEnum.CUSTOMER.getCode())
                    .setToken(token);
            if(baiduUser.getUsername()!=null){
                user.setUsername(baiduUser.getUsername());
            }else{
                user.setUsername("BaiDu"+baiduUser.getUserid());
            }
            if(baiduUser.getPortrait()!=null){
                user.setAvatarUrl("http://tb.himg.baidu.com/sys/portrait/item/"+baiduUser.getPortrait());
            }else{
                user.setAvatarUrl("https://shuixin.oss-cn-beijing.aliyuncs.com/tian.png");
            }

            Boolean crateOrUpdate = userService.crateOrUpdate(user);
            if(crateOrUpdate){
                return ResponseVo.success();
            }else{
                return ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR);
            }

        }else{
            //登录失败，请重新登录
            log.error("callback get github error,{}",baiduUser);
            return ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR);
        }
    }
}