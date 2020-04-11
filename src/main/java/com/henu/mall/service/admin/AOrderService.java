package com.henu.mall.service.admin;

import com.github.pagehelper.PageInfo;
import com.henu.mall.request.OrderUpdateRequest;
import com.henu.mall.vo.ResponseVo;

import java.util.Date;

/**
 * @author lv
 * @date 2020-04-04 12:31
 */
public interface AOrderService {
    /**
     * 订单列表 分页 按OrderNo 创建时间 收货人或手机号
     *
     * @return
     */
    ResponseVo<PageInfo> list(Long orderNo, Integer pageNum, Integer pageSize, String receiverNameOrPhone, Date time);



    /**
     * 更新订单
     * @param request
     * @return
     */
    ResponseVo update(OrderUpdateRequest request);




}
