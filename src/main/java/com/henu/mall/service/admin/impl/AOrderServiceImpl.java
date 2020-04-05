package com.henu.mall.service.admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.mapper.OrderExtMapper;
import com.henu.mall.mapper.UserExtMapper;
import com.henu.mall.pojo.Order;
import com.henu.mall.pojo.User;
import com.henu.mall.request.OrderUpdateRequest;
import com.henu.mall.service.admin.AOrderService;
import com.henu.mall.vo.AOrderVo;
import com.henu.mall.vo.ResponseVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lv
 * @date 2020-04-04 12:31
 */
@Service
public class AOrderServiceImpl implements AOrderService {

    @Resource
    private OrderExtMapper orderExtMapper;

    @Resource
    private UserExtMapper userExtMapper;
    /**
     * 订单列表 分页 按OrderNo 创建时间 收货人或手机号
     *
     * @param
     * @return
     */
    @Override
    public ResponseVo<PageInfo> list(Long orderNo, Integer pageNum, Integer pageSize, String receiverNameOrPhone, Date time) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderExtMapper.selectAll(orderNo, receiverNameOrPhone, time);
        Set<Integer> userIdSet =new HashSet<>();
        for (Order order : orderList) {
            userIdSet.add(order.getUserId());
        }
        List<User> userList = userExtMapper.selectUserListByUserIdSet(userIdSet);
        Map<Integer, String> userNameMap = userList.stream().collect(Collectors.toMap(User::getId,user -> user.getUsername()));
        List<AOrderVo> aOrderVoList = orderList.stream().map(order -> {
            AOrderVo aOrderVo = new AOrderVo();
            aOrderVo.setOrderNo(order.getOrderNo());
            aOrderVo.setPayment(order.getPayment());
            aOrderVo.setCloseTime(order.getCloseTime());
            aOrderVo.setCreateTime(order.getCreateTime());
            aOrderVo.setPaymentTime(order.getPaymentTime());
            aOrderVo.setPaymentType(order.getPaymentType());
            aOrderVo.setReceiverName(order.getReceiverName());
            aOrderVo.setStatus(order.getStatus());
            aOrderVo.setUserName(userNameMap.get(order.getUserId()));
            return aOrderVo;
        }).collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(aOrderVoList);
        return ResponseVo.success(pageInfo);
    }
    /**
     * 订单详情
     *
     * @param orderNo
     * @return
     */
    @Override
    public ResponseVo detail(Long orderNo) {
        return null;
    }

    /**
     * 更新订单
     *
     * @param request
     * @return
     */
    @Override
    public ResponseVo update(OrderUpdateRequest request) {
        return null;
    }

    /**
     * 删除订单
     *
     * @param orderNo
     * @return
     */
    @Override
    public ResponseVo delete(Integer orderNo) {
        return null;
    }
}
