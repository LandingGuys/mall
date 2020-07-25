package com.henu.mall.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.henu.mall.consts.MQConstant;
import com.henu.mall.dto.DLXMessage;
import com.henu.mall.service.member.MessageService;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-03-01 17:14
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 发送消息到队列
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */
    @Override
    public void send(String queueName, String message) {
        rabbitTemplate.convertAndSend(MQConstant.DEFAULT_EXCHANGE,queueName, message);
    }

    /**
     * 延迟发送消息到队列
     *
     * @param queueName 队列名称
     * @param message   消息内容
     * @param times     延迟时间 单位毫秒
     */
    @Override
    public void send(String queueName, String message, long times) {
        //消息发送到死信队列上，当消息超时时，会发生到转发队列上，转发队列根据下面封装的queueName，把消息转发的指定队列上
        //发送前，把消息进行封装，转发时应转发到指定 queueName 队列上
        DLXMessage dlxMessage = new DLXMessage(MQConstant.DEFAULT_EXCHANGE,queueName,message,times);
        MessagePostProcessor processor = e ->{
              e.getMessageProperties().setExpiration(times + "");
              return e;
          };
        String content = JSON.toJSONString(dlxMessage);
        rabbitTemplate.convertAndSend(MQConstant.DEFAULT_EXCHANGE,MQConstant.DEFAULT_DEAD_LETTER_QUEUE_NAME,content, processor);
    }
}
