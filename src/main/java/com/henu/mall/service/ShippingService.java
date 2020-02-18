package com.henu.mall.service;

import com.github.pagehelper.PageInfo;
import com.henu.mall.request.ShippingRequest;
import com.henu.mall.vo.ResponseVo;

import java.util.Map;

/**
 * @author lv
 * @date 2020-02-13 8:25
 */
public interface ShippingService {
    /**
     * 新增收货地址
     * @param uid
     * @param request
     * @return
     */
    ResponseVo<Map<String,Integer>> add(Integer uid, ShippingRequest request);

    /**
     * 删除收货地址
     * @param uid
     * @param shippingId
     * @return
     */
    ResponseVo delete(Integer uid,Integer shippingId);

    /**
     * 更新收货地址
     * @param uid
     * @param shippingId
     * @return
     */
    ResponseVo update(Integer uid, Integer shippingId, ShippingRequest request);

    /**
     * 收货地址分页列表查询
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid,Integer pageNum,Integer pageSize);
}
