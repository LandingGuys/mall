package com.henu.mall.manager;

import com.henu.mall.vo.UserVo;

/**
 * @author lv
 * @date 2020-02-22 7:59
 */
public interface TokenManager {
    /**
     * 创建token
     * @param userVo
     * @return
     */
    String getToken(UserVo userVo);

    /**
     * 验证token
     * @param tokenValue
     * @return
     */
    Boolean validateToken(String tokenValue);

    /**
     * 刷新用户
     * @param tokenValue
     */
    void refreshUserToken(String tokenValue);

    /**
     * 用户退出登录
     * @param token
     */
    void loginOff(String token);

//    /**
//     * 获取用户信息
//     * @param token
//     * @return
//     */
//    UserVo getUserVoByToken(String token);
}
