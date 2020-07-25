package com.henu.mall.manager;

import com.henu.mall.consts.MallConsts;
import com.henu.mall.utils.RedisUtil;
import com.henu.mall.utils.TokenGenerator;
import com.henu.mall.vo.UserVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.henu.mall.consts.MallConsts.USER_TOKEN_KEY_EXPIRE_TIME;

/**
 * @author lv
 * @date 2020-02-22 8:03
 */
@Component
public class RedisTokenManager implements TokenManager{

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private TokenGenerator tokenGenerator;
    /**
     * 创建token
     * redis 存 token_userId - tokenValue
     *          tokenValue  -token_userId
     * @param userVo
     * @return
     */
    @Override
    public String getToken(UserVo userVo) {
        String userId = userVo.getId().toString();
        String token_format = String.format(MallConsts.USER_TOKEN_KEY_TEMPLATE,userId);
        String token_rest_format =String.format(MallConsts.USER_TOKEN_KEY_RESET_TEMPLATE,userId);

        String token = tokenGenerator.generate(userVo.getUsername());
        redisUtil.setEx(token_format,token,USER_TOKEN_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
        redisUtil.setEx(token,token_format,USER_TOKEN_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
        Long currentTime = System.currentTimeMillis();
        redisUtil.set(token_rest_format,currentTime.toString());

        return token;
    }

    /**
     * 刷新用户
     *
     * @param tokenValue
     */
    @Override
    public void refreshUserToken(String tokenValue) {
            String token = redisUtil.get(tokenValue);
            String token_rest_format=String.format(
                    MallConsts.USER_TOKEN_KEY_RESET_TEMPLATE,
                    token.split("\\_")[1]);
            Long tokeBirthTime = Long.valueOf(redisUtil.get(token_rest_format));
            Long diff =System.currentTimeMillis() -tokeBirthTime;
            if(diff > MallConsts.USER_TOKEN_KEY_RESET_TIME){
                redisUtil.expire(token,USER_TOKEN_KEY_EXPIRE_TIME,TimeUnit.SECONDS);
                redisUtil.expire(tokenValue,USER_TOKEN_KEY_EXPIRE_TIME,TimeUnit.SECONDS);
                Long newBirthTime = System.currentTimeMillis();
                redisUtil.set(token_rest_format,newBirthTime.toString());

            }
    }

    /**
     * 用户退出登录
     *
     * @param tokenValue
     */
    @Override
    public void loginOff(String tokenValue) {
        String token = redisUtil.get(tokenValue);
        String token_rest_format=String.format(
                MallConsts.USER_TOKEN_KEY_RESET_TEMPLATE,
                token.split("\\_")[1]);
        redisUtil.delete(token);
        redisUtil.delete(tokenValue);
        redisUtil.delete(token_rest_format);
    }

    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
//    @Override
//    public UserVo getUserVoByToken(String token) {
//        token = String.format(MallConsts.USER_TOKEN_KEY_TEMPLATE,token);
//        if(redisUtil.hasKey(token)){
//           return redisUtil.get(token);
//
//        }
//        return null;
//    }

    /**
     * 验证token
     *
     * @param tokenValue
     * @return
     */
    @Override
    public Boolean validateToken(String tokenValue) {
        if(redisUtil.hasKey(tokenValue)){
            String token = redisUtil.get(tokenValue);
            if(redisUtil.hasKey(token)) {
                return true;
            }
        }

        return false;
    }
}
