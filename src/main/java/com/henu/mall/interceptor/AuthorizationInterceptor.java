package com.henu.mall.interceptor;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.consts.MallConsts;
import com.henu.mall.exception.UserLoginException;
import com.henu.mall.exception.UserTokenException;
import com.henu.mall.manager.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author lv
 * @date 2020-01-21 9:19
 */
@Service
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Resource
    private TokenManager tokenManager;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Boolean hasToken = false;
        //判断请求方法是否标注@AuthIgnore注解
        if (method.getAnnotation(AuthIgnore.class) != null || handlerMethod.getBeanType().getAnnotation(AuthIgnore.class) != null) {
            //从请求头中获取token值
            String tokenValue=request.getHeader(MallConsts.HTTP_HEADER_NAME);
            log.info("GET Token from request is {}",tokenValue);
            //判断请求头中获取的token值是否为空
            if(StringUtils.isNotBlank(tokenValue)){
                    //从redis中验证token值，是否存在，是否过期
                   hasToken = tokenManager.validateToken(tokenValue);
            }else{
                // 空,返回错误信息，跳转到登录页
                   throw  new UserLoginException();
            }
            if(!hasToken){
                // 验证失败，返回错误信息，跳转到登录页
                throw new UserTokenException();
            }

            //验证成功，重新设置token过期时间
            tokenManager.refreshUserToken(tokenValue);
        }

        return true;
    }
}
