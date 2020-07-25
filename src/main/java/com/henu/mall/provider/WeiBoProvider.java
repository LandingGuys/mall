package com.henu.mall.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.henu.mall.dto.WeiBoAccessTokenDTO;
import com.henu.mall.dto.WeiBoUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lv
 * @date 2020-04-16 10:48
 */
@Component
@Slf4j
public class WeiBoProvider {

    public static String requestOauthUrl(WeiBoAccessTokenDTO weiBoAccessTokenDTO){
        String oauthUrl="https://api.weibo.com/oauth2/authorize?client_id="
                +weiBoAccessTokenDTO.getClientId()+"&response_type=code&redirect_uri="+weiBoAccessTokenDTO.getRedirectUri();
        return oauthUrl;
    }

    public static String getAccessTokenAndUid(WeiBoAccessTokenDTO weiBoAccessTokenDTO) {
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String s = "grant_type="+weiBoAccessTokenDTO.getGrant_type()+"&code="+weiBoAccessTokenDTO.getCode()+"&client_id="+weiBoAccessTokenDTO.getClientId()+"&client_secret="+weiBoAccessTokenDTO.getClientSecret()+"&redirect_uri="+weiBoAccessTokenDTO.getRedirectUri();
        RequestBody body = RequestBody.create(mediaType, s);
        Request request = new Request.Builder()
                .url("https://api.weibo.com/oauth2/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //TODO 需要改
            String string = response.body().string();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WeiBoUser getUser(String accessTokenAndUid) {
        JSONObject object = JSON.parseObject(accessTokenAndUid);
        String accessToken = object.getString("access_token");
        String uid = object.getString("uid");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.weibo.com/2/users/show.json?access_token=" + accessToken+"&uid="+uid).build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            WeiBoUser weiBoUser= JSON.parseObject(string, WeiBoUser.class);
            return weiBoUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
