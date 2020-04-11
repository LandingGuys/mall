package com.henu.mall.service.admin.impl;

import com.henu.mall.enums.OrderStatusEnum;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.mapper.LogisticsExtMapper;
import com.henu.mall.mapper.OrderMapper;
import com.henu.mall.pojo.Logistics;
import com.henu.mall.pojo.Order;
import com.henu.mall.pojo.OrderExample;
import com.henu.mall.request.LogisticsAddRequest;
import com.henu.mall.service.admin.TransportationService;
import com.henu.mall.vo.ALogisticsVo;
import com.henu.mall.vo.ResponseVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author lv
 * @date 2020-04-10 16:41
 */
@Service
public class TransportationServiceImpl implements TransportationService {

    @Resource
    private LogisticsExtMapper logisticsExtMapper;

    @Resource
    private OrderMapper orderMapper;

    @Transactional
    @Override
    public ResponseVo add(LogisticsAddRequest requesut) {
        Logistics logisticsFormDb = logisticsExtMapper.get(requesut.getOrderNo());
        if(logisticsFormDb != null){
            return ResponseVo.error(ResponseEnum.LOGISTICS_IS_EXIST);
        }
        Logistics logistics = new Logistics();
        BeanUtils.copyProperties(requesut,logistics);
        //写入物流信息
        int row = logisticsExtMapper.insertSelective(logistics);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.LOGISTICS_INSERT_ERROR);
        }
        //变更订单状态
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoEqualTo(requesut.getOrderNo());
        List<Order> orderList = orderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(orderList)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        Order order = orderList.get(0);
        if(!order.getStatus().equals(OrderStatusEnum.PAID.getCode())){
           return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusEnum.SHIPPED.getCode());
        order.setSendTime(new Date());
        int rowOrder = orderMapper.updateByPrimaryKey(order);
        if(rowOrder <= 0){
            return ResponseVo.error(ResponseEnum.ORDER_UPDATE_ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo get(Long orderNo) {
        Logistics logistics = logisticsExtMapper.get(orderNo);
        if(logistics ==null){
            return ResponseVo.error(ResponseEnum.LOGISTICS_NOT_EXIST);
        }
        ALogisticsVo aLogisticsVo = new ALogisticsVo();
        BeanUtils.copyProperties(logistics,aLogisticsVo);
        return ResponseVo.success(aLogisticsVo);
    }
}
