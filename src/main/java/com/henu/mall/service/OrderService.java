package com.henu.mall.service;

import com.github.pagehelper.PageInfo;
import com.henu.mall.vo.OrderVo;
import com.henu.mall.vo.ResponseVo;

/**
 * @author lv
 * @date 2020-02-13 16:27
 */
public interface OrderService {
    /**
     * 创建订单
     * @param uid
     * @param shippingId
     * @return
     */
    ResponseVo<OrderVo> create(Integer uid,Integer shippingId);

    /**
     * 订单列表
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    /**
     * 订单详情
     * @param uid
     * @param orderNo
     * @return
     */
    ResponseVo<OrderVo> detail(Integer uid,Long orderNo);

    /**
     * 取消订单
     * @param uid
     * @param orderNo
     * @return
     */
    ResponseVo cancel(Integer uid,Long orderNo);

    /**
     * 支付订单
     */

}
