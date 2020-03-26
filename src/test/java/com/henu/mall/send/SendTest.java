package com.henu.mall.send;

import com.henu.mall.MallApplicationTests;
import com.henu.mall.consts.MQConstant;
import com.henu.mall.service.member.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author lv
 * @date 2020-03-01 17:33
 */
@Slf4j
public class SendTest extends MallApplicationTests {

    @Resource
    private MessageService messageService;
    @Test
    public void send(){

        //log.info("发送时间：",System.currentTimeMillis());
        System.out.println("发送时间"+ new Date());
        String message = "测试延迟消息";

        messageService.send(MQConstant.ORDER_QUEUE_NAME,message,6000);

        message = "测试普通消息";
        messageService.send(MQConstant.ORDER_QUEUE_NAME,message);
    }
}
