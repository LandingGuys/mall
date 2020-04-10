package com.henu.mall.listener;

import com.henu.mall.consts.MQConstant;
import com.henu.mall.service.member.OrderService;
import com.henu.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-03-01 17:24
 * 接受超时order 消息 超时order 将自动取消订单
 */
@Slf4j
@Component
@RabbitListener(queues = MQConstant.ORDER_QUEUE_NAME)
public class OrderMsgListener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void process(String content){
        log.info("【接收到消息】 => {}",content);
        Long orderNo = Long.parseLong(content);
        ResponseVo responseVo = orderService.cancel(orderNo);
        log.info("【自动取消订单结果】 => {}",responseVo);
    }
}
