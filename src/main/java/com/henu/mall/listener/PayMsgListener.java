package com.henu.mall.listener;

import com.alibaba.fastjson.JSON;
import com.henu.mall.pojo.PayInfo;
import com.henu.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-02-20 12:20
 */
@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void process(String msg){
        log.info("【接收到消息】 => {}",msg);
        try {
            PayInfo payInfo = JSON.parseObject(msg, PayInfo.class);
            if (payInfo.getPlatformStatus().equals("SUCCESS")){
                //修改订单里的状态
                orderService.paid(payInfo.getOrderNo());
            }
        } catch (RuntimeException e){
            throw new RuntimeException("异步消息体错误");
        }

    }
}
