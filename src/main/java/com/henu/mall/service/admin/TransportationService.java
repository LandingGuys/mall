package com.henu.mall.service.admin;

import com.henu.mall.request.LogisticsAddRequest;
import com.henu.mall.vo.ResponseVo;

/**
 * @author lv
 * @date 2020-04-10 16:41
 */
public interface TransportationService {

    ResponseVo add(LogisticsAddRequest requesut);

    ResponseVo get(Long orderNo);


    ResponseVo getTrack(String expCode, String expNo);


}
