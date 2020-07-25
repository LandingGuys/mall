package com.henu.mall.controller.merber;

import com.alibaba.fastjson.JSONObject;
import com.henu.mall.dto.*;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.RoleEnum;
import com.henu.mall.pojo.User;
import com.henu.mall.provider.BaiDuProvider;
import com.henu.mall.provider.QQProvider;
import com.henu.mall.provider.WeiBoProvider;
import com.henu.mall.service.member.UserService;
import com.henu.mall.vo.OauthVo;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Api(description = "前台第三方登录服务接口")
public class Oauth2Controller {
    @Resource
    private UserService userService;

    @Resource
    private WeiBoAccessTokenDTO weiBoAccessTokenDTO;

    @Resource
    private QQAccessTokenDTO qqAccessTokenDTO;

    @Resource
    private BaiDuAccessTokenDTO baiDuAccessTokenDTO;


    @ApiOperation("获取第三方登录地址")
    @PostMapping("/oauth/{req}")
    public ResponseVo<OauthVo> oauth(@PathVariable("req") String req) throws IOException{
        OauthVo oauthVo =new OauthVo();
        if("qq".equals(req)){
            String requestQqOauthUrl = QQProvider.requestOauthUrl(qqAccessTokenDTO);
            oauthVo.setUrl(requestQqOauthUrl);
            return ResponseVo.success(oauthVo);
        }else if ("baidu".equals(req)){
            String requestBaiDuOauthUrl = BaiDuProvider.requestOauthUrl(baiDuAccessTokenDTO);
            oauthVo.setUrl(requestBaiDuOauthUrl);
            return ResponseVo.success(oauthVo);
        }else if("weibo".equals(req)){
            String requestWeiBoOauthUrl = WeiBoProvider.requestOauthUrl(weiBoAccessTokenDTO);
            oauthVo.setUrl(requestWeiBoOauthUrl);
            return ResponseVo.success(oauthVo);
        }
       return null;
    }

    @ApiOperation("qq登录回调")
    @GetMapping("/qqcallback")
    public void qqCallback(@RequestParam("code") String code,
                                         HttpSession session,HttpServletResponse response) throws IOException {
        qqAccessTokenDTO.setCode(code);
        String accessToken= QQProvider.getAccessToken(qqAccessTokenDTO);
        String openId=QQProvider.getOpenId(accessToken);
        QQUser qqUser=QQProvider.getUserInfo(openId,qqAccessTokenDTO.getClient_id(),accessToken);
        if(qqUser !=null && openId !=null){
            User user = new User();
            user.setAccountId(openId);
            user.setRole(RoleEnum.CUSTOMER.getCode());
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
                JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_QQ_ERROR));
                response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
            }
            //设置Session
            session.setAttribute("user", userResponseVo.getData());
            log.info("/login sessionId={}", session.getId());
            //区别于Baidu第三方登录 因为qq用户头像地址 http://thirdqq.qlogo.cn/g?b=oidb&k=I5yrgygI28K7B2ibAsCKdIw&s=40&t=1557546068
            //& 在前端 获取jsonStu 出现丢失。所以 重新设置Vo对象只传token id 两个字段
            UserVo qqUserVo=new UserVo();
            qqUserVo.setToken(userResponseVo.getData().getToken());
            qqUserVo.setId(userResponseVo.getData().getId());
            ResponseVo.success(qqUserVo);
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON( ResponseVo.success(qqUserVo));
            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }else{
            //登录失败，请重新登录
            log.error("callback get qq error,{}",qqUser);
            //ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR);
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_QQ_ERROR));
            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }

    }

    @ApiOperation("weibo登录回调")
    @GetMapping("/weibocallback")
    public void weiBoCallback(@RequestParam("code") String code,
                              HttpSession session,HttpServletResponse response) throws IOException{
        weiBoAccessTokenDTO.setCode(code);
        String accessTokenAndUid = WeiBoProvider.getAccessTokenAndUid(weiBoAccessTokenDTO);
        WeiBoUser weiBoUser = WeiBoProvider.getUser(accessTokenAndUid);

        if(weiBoUser !=null && weiBoUser.getIdstr() !=null){
            User user =new User();
            user.setAccountId(weiBoUser.getIdstr());
            user.setRole(RoleEnum.CUSTOMER.getCode());
            if(weiBoUser.getName() != null){
                user.setUsername(weiBoUser.getName());
            }else{
                user.setUsername("WeiBo"+weiBoUser.getIdstr());
            }
            if(weiBoUser.getAvatar_hd() !=null){
                user.setAvatarUrl(weiBoUser.getAvatar_hd());
            }else{
                user.setAvatarUrl("http://shuixin.oss-cn-beijing.aliyuncs.com/moren.jpg");
            }
            ResponseVo<UserVo> userResponseVo = userService.crateOrUpdate(user);
            if(userResponseVo.getStatus().equals(ResponseEnum.THIRD_PARTY_LOGIN_ERROR.getCode())){
                JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_QQ_ERROR));
                response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
            }
            //设置Session
            session.setAttribute("user", userResponseVo.getData());
            log.info("/login sessionId={}", session.getId());
            //区别于Baidu第三方登录 因为weibo用户头像地址 http://thirdqq.qlogo.cn/g?b=oidb&k=I5yrgygI28K7B2ibAsCKdIw&s=40&t=1557546068
            //& 在前端 获取jsonStu 出现丢失。所以 重新设置Vo对象只传token id 两个字段
            UserVo weiBoUserVo=new UserVo();
            weiBoUserVo.setToken(userResponseVo.getData().getToken());
            weiBoUserVo.setId(userResponseVo.getData().getId());
            ResponseVo.success(weiBoUserVo);
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON( ResponseVo.success(weiBoUserVo));
            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }else{
            //登录失败，请重新登录
            log.error("callback get WeiBo error,{}",weiBoUser);
            //ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR);
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_WEI_BO_ERROR));
            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }
    }
    @ApiOperation("baidu登录回调")
    @GetMapping("/baiducallback")
    public void baiDuCallBack(@RequestParam("code") String code,
                              HttpServletResponse response,HttpSession session) throws IOException {
        baiDuAccessTokenDTO.setCode(code);
        String accessToken = BaiDuProvider.getAccessToken(baiDuAccessTokenDTO);
        BaiDuUser baiduUser=BaiDuProvider.getUser(accessToken);
        if(baiduUser!=null && baiduUser.getUserid()!=null){
            User user=new User();
            user.setAccountId(baiduUser.getUserid());
            user.setRole(RoleEnum.CUSTOMER.getCode());
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
                JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR));
                response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
            }
            session.setAttribute("user", userResponseVo.getData());
            log.info("/login sessionId={}", session.getId());
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON(userResponseVo);
            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }else{
            //登录失败，请重新登录
            log.error("callback get baidu error,{}",baiduUser);
            //ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR);
            JSONObject jsonStu= (JSONObject) JSONObject.toJSON(ResponseVo.error(ResponseEnum.THIRD_PARTY_LOGIN_BAI_DU_ERROR));
            response.sendRedirect("http://www.mall.wast.club/#/oauth?result=" + jsonStu);
        }
    }
}