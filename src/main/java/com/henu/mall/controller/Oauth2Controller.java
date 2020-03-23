package com.henu.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.henu.mall.dto.BaiDuAccessTokenDTO;
import com.henu.mall.dto.BaiDuUser;
import com.henu.mall.dto.QQAccessTokenDTO;
import com.henu.mall.dto.QQUser;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.manager.AuthManager;
import com.henu.mall.pojo.User;
import com.henu.mall.provider.BaiDuProvider;
import com.henu.mall.provider.QQProvider;
import com.henu.mall.service.UserService;
import com.henu.mall.vo.OauthVo;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    @Resource
    private AuthManager authManager;

    @PostMapping("/oauth/{req}")
    public ResponseVo<OauthVo> oauth(@PathVariable("req") String req, HttpServletResponse response) throws IOException{
        OauthVo oauthVo =new OauthVo();
        if("qq".equals(req)){
            QQAccessTokenDTO qqAccessTokenDTO = new QQAccessTokenDTO();
            qqAccessTokenDTO.setClient_id(QQClientId)
                    .setRedirect_uri(QQRedirectUri);
            String requestQqOauthUrl = QQProvider.requestOauthUrl(qqAccessTokenDTO);
            //重定向
            //response.sendRedirect(requestQqOauthUrl);
            oauthVo.setUrl(requestQqOauthUrl);
            return ResponseVo.success(oauthVo);

        }else if ("baidu".equals(req)){
            BaiDuAccessTokenDTO baiDuAccessTokenDTO = new BaiDuAccessTokenDTO();
            baiDuAccessTokenDTO.setClient_id(BaiDuClientId)
                    .setRedirect_uri(BaiDuRedirectUri);
            String requestBaiDuOauthUrl = BaiDuProvider.requestOauthUrl(baiDuAccessTokenDTO);
            //重定向
            //response.sendRedirect(requestBaiDuOauthUrl);
            oauthVo.setUrl(requestBaiDuOauthUrl);
            return ResponseVo.success(oauthVo);
        }else if("weixin".equals(req)){

        }else{

        }
       return null;
    }
    @GetMapping("/qqcallback")
    public void qqCallback(@RequestParam("code") String code,
                                         HttpSession session,HttpServletResponse response) throws IOException {
        QQAccessTokenDTO qqAccessTokenDTO = new QQAccessTokenDTO("authorization_code", QQClientId, code, QQClientSecret, QQRedirectUri);
        String accessToken= QQProvider.getAccessToken(qqAccessTokenDTO);
        String openId=QQProvider.getOpenId(accessToken);
        QQUser qqUser=QQProvider.getUserInfo(openId,QQClientId,accessToken);
        User user = new User();
        user.setAccountId(openId)
                .setRole(RoleEnum.CUSTOMER.getCode());
        if(qqUser.getNickname()==null){
            user.setUsername("qq"+openId);
        }else{
            user.setUsername(qqUser.getNickname());
        }
        if(StringUtils.isNoneBlank(qqUser.getFigureurl_qq_1())){
            user.setAvatarUrl(qqUser.getFigureurl_qq_1());
        }else{
            user.setAvatarUrl("http://shuixin.oss-cn-beijing.aliyuncs.com/moren.jpg");
        }
        ResponseVo<UserVo> userResponseVo = userService.crateOrUpdate(user);
        if(userResponseVo.getStatus().equals(ResponseEnum.THIRD_PARTY_LOGIN_ERROR.getCode())){
            //return ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_QQ_ERROR);
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_QQ_ERROR));
            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }
        //authManager.login(userResponseVo.getData());
        //response.addCookie(new Cookie("token",userResponseVo.getData().getToken()));
        //设置Session
        session.setAttribute("user", userResponseVo.getData());
        log.info("/login sessionId={}", session.getId());
        //区别于Baidu第三方登录 因为qq用户头像地址 http://thirdqq.qlogo.cn/g?b=oidb&k=I5yrgygI28K7B2ibAsCKdIw&s=40&t=1557546068
        // & 在前端 获取jsonStu 出现丢失。所以 重新设置Vo对象只传token id 两个字段
        UserVo qqUserVo=new UserVo();
        qqUserVo.setToken(userResponseVo.getData().getToken());
        qqUserVo.setId(userResponseVo.getData().getId());
        ResponseVo.success(qqUserVo);
        JSONObject jsonStu= (JSONObject) JSONObject.toJSON( ResponseVo.success(qqUserVo));
        response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        //return userResponseVo;
    }
    @PostMapping("/weiBoCallBack")
    public ResponseVo weiBoCallback(){
        return null;
    }
    @GetMapping("/baiducallback")
    public void baiDuCallBack(@RequestParam("code") String code,
                              HttpServletResponse response,HttpSession session) throws IOException {
        BaiDuAccessTokenDTO baiDuAccessTokenDTO = new BaiDuAccessTokenDTO("authorization_code", BaiDuClientId, code, BaiDuClientSecret, BaiDuRedirectUri);
        String accessToken = BaiDuProvider.getAccessToken(baiDuAccessTokenDTO);

        BaiDuUser baiduUser=BaiDuProvider.getUser(accessToken);
        if(baiduUser!=null && baiduUser.getUserid()!=null){

            User user=new User();
            user.setAccountId(baiduUser.getUserid())
                    .setRole(RoleEnum.CUSTOMER.getCode());

            if(baiduUser.getUsername()!=null){
                user.setUsername(baiduUser.getUsername());
            }else{
                user.setUsername("BaiDu"+baiduUser.getUserid());
            }
            if(baiduUser.getPortrait()!=null){
                user.setAvatarUrl("http://tb.himg.baidu.com/sys/portrait/item/"+baiduUser.getPortrait());
            }else{
                user.setAvatarUrl("http://shuixin.oss-cn-beijing.aliyuncs.com/moren.jpg");
            }

            ResponseVo<UserVo> userResponseVo = userService.crateOrUpdate(user);
            if(userResponseVo.getStatus().equals(ResponseEnum.THIRD_PARTY_LOGIN_ERROR.getCode())){
                //ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR);
                JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR));
                response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
            }
            //authManager.login(userResponseVo.getData());
            session.setAttribute("user", userResponseVo.getData());
            log.info("/login sessionId={}", session.getId());
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON(userResponseVo);

            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }else{
            //登录失败，请重新登录
            log.error("callback get github error,{}",baiduUser);
            //ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR);
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR));
            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }
    }
}