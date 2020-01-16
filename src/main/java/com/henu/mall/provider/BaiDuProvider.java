package com.henu.mall.provider;

import com.alibaba.fastjson.JSON;
import com.henu.mall.dto.BaiDuAccessTokenDTO;
import com.henu.mall.dto.BaiDuUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lvbenwei11319
 * @date  2020/1/16 15:41
 * @desc 
 */
@Component
public class BaiDuProvider {
    public static String requestOauthUrl(BaiDuAccessTokenDTO accessTokenDTO){
        String oauthUrl=" https://openapi.baidu.com/oauth/2.0/authorize?response_type=code&client_id="
                +accessTokenDTO.getClient_id()+"&redirect_uri="
                +accessTokenDTO.getRedirect_uri()+"&display=popup";
        return oauthUrl;
    }

    public static String getAccessToken(BaiDuAccessTokenDTO accessTokenDTO) {
        OkHttpClient client = new OkHttpClient();
        String urlString = "https://openapi.baidu.com/oauth/2.0/token?grant_type=" + accessTokenDTO.getGrant_type() +
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
    public static BaiDuUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://openapi.baidu.com/rest/2.0/passport/users/getInfo?access_token=" + accessToken).build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            BaiDuUser baiduUser = JSON.parseObject(string, BaiDuUser.class);
            return baiduUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}