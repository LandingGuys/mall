package com.henu.mall.listener;

import com.alibaba.fastjson.JSON;
import com.henu.mall.consts.MQConstant;
import com.henu.mall.dto.DLXMessage;
import com.henu.mall.service.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-03-01 17:28
 *
 *监听转发队列,有消息时，把消息转发到目标队列
 */
@Component
@RabbitListener(queues = MQConstant.DEFAULT_REPEAT_TRADE_QUEUE_NAME)
public class ReceiverDelayMessage {
    @Resource
    private MessageService messageService;
    @RabbitHandler
    public void process(String content){
        //此时，才把消息发送到指定队列，而实现延迟功能
        DLXMessage message = JSON.parseObject(content,DLXMessage.class);
        messageService.send(message.getQueueName(),message.getContent());
    }
}
