package com.henu.mall.service;

/**
 * @author lv
 * @date 2020-03-01 17:12
 */
public interface MessageService {

    /**
     * 发送消息到队列
     * @param queueName 队列名称
     * @param message 消息内容
     */
    void send(String queueName,String message);


    /**
     * 延迟发送消息到队列
     * @param queueName 队列名称
     * @param message 消息内容
     * @param times 延迟时间 单位毫秒
     */
    void send(String queueName,String message,long times);
}
