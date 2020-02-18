package com.henu.mall.interceptor;

import com.henu.mall.consts.MallConsts;
import com.henu.mall.exception.UserLoginException;
import com.henu.mall.exception.UserTokenException;
import com.henu.mall.mapper.UserMapper;
import com.henu.mall.pojo.User;
import com.henu.mall.pojo.UserExample;
import com.henu.mall.utils.RedisUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lv
 * @date 2020-01-21 9:19
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length!=0){
            for (Cookie cookie : cookies) {
                if("token".equals(cookie.getName())){
                    String token = cookie.getValue();
                    String username="";
                    if(token !=null && token.length() !=0){
                        if(!redisUtil.hasKey(token)){
                            throw new UserTokenException();
                        }
                        username =redisUtil.get(token);
                    }
                    if(username !=null && !username.trim().equals("")){
                        String tokenKey =String.format(MallConsts.USER_TOKEN_KEY_TEMPLATE,username);
                        Long tokenBirthTime = Long.valueOf(redisUtil.get(tokenKey));
                        Long diff = System.currentTimeMillis() -tokenBirthTime;
                        //重新设置Redis中token 过期时间
                        if(diff > MallConsts.USER_TOKEN_KEY_RESET_TIME){
                            redisUtil.expire(username,MallConsts.USER_TOKEN_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
                            redisUtil.expire(token,MallConsts.USER_TOKEN_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
                            Long newBirthTime = System.currentTimeMillis();
                            redisUtil.set(tokenKey,newBirthTime.toString());
                        }
                    }
                    UserExample example = new UserExample();
                    example.createCriteria().andUsernameEqualTo(username);
                    List<User> users = userMapper.selectByExample(example);

                    if(CollectionUtils.isEmpty(users)){
                        throw new UserLoginException();
                    }
                    request.getSession().setAttribute("user",users.get(0));
                    break;
                }else{
                    throw new UserLoginException();
                }
            }
        }else{
            throw new UserLoginException();
        }

        return true;
    }
}
