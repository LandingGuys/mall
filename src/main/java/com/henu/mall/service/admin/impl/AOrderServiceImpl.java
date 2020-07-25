package com.henu.mall.service.admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.mapper.OrderExtMapper;
import com.henu.mall.mapper.OrderMapper;
import com.henu.mall.mapper.UserExtMapper;
import com.henu.mall.pojo.Order;
import com.henu.mall.pojo.OrderExample;
import com.henu.mall.pojo.User;
import com.henu.mall.request.OrderUpdateRequest;
import com.henu.mall.service.admin.AOrderService;
import com.henu.mall.vo.AOrderVo;
import com.henu.mall.vo.ResponseVo;
import org.apache.commons.collections4.CollectionUtils;
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

    @Resource
    private OrderMapper orderMapper;
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
     * 更新订单
     *
     * @param request
     * @return
     */
    @Override
    public ResponseVo update(OrderUpdateRequest request) {

        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoEqualTo(request.getOrderNo());
        List<Order> orderList = orderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(orderList)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        Order order = orderList.get(0);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        int row = orderMapper.updateByPrimaryKey(order);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.ORDER_UPDATE_ERROR);
        }
        return ResponseVo.success();
    }


}
