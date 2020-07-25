package com.henu.mall.manager;

import com.henu.mall.consts.MallConsts;
import com.henu.mall.vo.UserVo;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lv
 * @date 2020-02-22 8:21
 */
@Component
public class AuthManager {
    @Resource
    private TokenManager tokenManager;

    /**
     * 获取请求体
     * @return
     */
    public HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 登录生成token
     * @param userVo
     * @return
     */
    public String login(UserVo userVo){
        return tokenManager.getToken(userVo);
    }

//    /**
//     * 获取该用户信息
//     * @return
//     */
//    public UserVo getUserVo(){
//        HttpServletRequest request =getRequest();
//        String token = request.getAttribute(MallConsts.USER_TOKEN).toString();
//        UserVo userVo = tokenManager.getUserVoByToken(token);
//        if(userVo == null){
//            throw new UserTokenException();
//        }
//        return userVo;
//    }
    /**
     * 刷新该登录用户，延时
     */
    public void refreshUserInfo(){
        HttpServletRequest request=getRequest();

        String token=request.getHeader(MallConsts.HTTP_HEADER_NAME);
        tokenManager.refreshUserToken(token);
    }
    /**
     * 注销该访问用户
     */
    public void loginOff(){
        HttpServletRequest request=getRequest();
        String tokenValue=request.getHeader(MallConsts.HTTP_HEADER_NAME);
//        String tokenValue=request.getParameter(MallConsts.HTTP_HEADER_NAME);
        tokenManager.loginOff(tokenValue);
    }

}
