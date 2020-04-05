package com.henu.mall.service.member;

import com.github.pagehelper.PageInfo;
import com.henu.mall.request.OrderCreateRequest;
import com.henu.mall.vo.OrderVo;
import com.henu.mall.vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lv
 * @date 2020-02-13 16:27
 */
public interface OrderService {
    /**
     * 创建订单
     * @param uid
     * @param request
     * @return
     */
    ResponseVo<OrderVo> create(Integer uid, OrderCreateRequest orderCreateRequest, HttpServletRequest request);

    /**
     * 订单列表 用户
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    /**
     * 订单详情 用户
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
     * 删除订单
     * @param uid
     * @param orderNo
     * @return
     */
    ResponseVo delete(Integer uid,Long orderNo);

    /**
     * 支付后修改订单状态
     */
    void paid(Long orderNo,Integer payPlatform);

    /**
     * 超时自动取消订单
     * @param orderNo
     * @return
     */
    void cancel(Long orderNo);

}
