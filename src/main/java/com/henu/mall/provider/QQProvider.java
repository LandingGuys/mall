package com.henu.mall.provider;

import com.alibaba.fastjson.JSON;
import com.henu.mall.dto.QQAccessTokenDTO;
import com.henu.mall.dto.QQOpenId;
import com.henu.mall.dto.QQUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lvbenwei11319
 * @date  2020/1/10 19:39
 * @desc 
 */
@Component
public class QQProvider {
    /**
     * 获取accessToken
     * @param accessTokenDTO
     * @return
     */
    public static String getAccessToken(QQAccessTokenDTO accessTokenDTO) {
        OkHttpClient client = new OkHttpClient();
        String urlString = "https://graph.qq.com/oauth2.0/token?grant_type=" + accessTokenDTO.getGrant_type() +
                "&code=" + accessTokenDTO.getCode() + "&client_id=" + accessTokenDTO.getClient_id() + "&client_secret=" +
                accessTokenDTO.getClient_secret() + "&redirect_uri=" + accessTokenDTO.getRedirect_uri();
        Request request = new Request.Builder().url(urlString).get().build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String accessToken = JSON.parseObject(string).getString("access_token");
            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getOpenId(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://graph.qq.com/oauth2.0/me?" + accessToken).build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
            String qqString=string.split("\\(")[1].split("\\)")[0];
            QQOpenId qqOpenId = JSON.parseObject(qqString, QQOpenId.class);
            return qqOpenId.getOpenid();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取QQuser信息
     * @param accessToken
     * @return
     */
    public static QQUser getUserInfo(String openId, String qq_clientId, String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://graph.qq.com/user/get_user_info?"+accessToken+"&oauth_consumer_key="+qq_clientId+"&openid="+openId+"").build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            QQUser qqUser = JSON.parseObject(string, QQUser.class);
            return qqUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}