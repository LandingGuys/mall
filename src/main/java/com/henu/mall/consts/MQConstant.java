package com.henu.mall.consts;

/**
 * @author lv
 * @date 2020-03-01 16:52
 */
public class MQConstant {
    //exchange name
    public static final String DEFAULT_EXCHANGE = "MaChange";

    //TTL QUEUE
    public static final String DEFAULT_DEAD_LETTER_QUEUE_NAME ="Ma.dead.letter.queue";

    //DLX repeat QUEUE 死信转发队列
    public static final String DEFAULT_REPEAT_TRADE_QUEUE_NAME ="Ma.repeat.trade.queue";

    // order 超时处理队列
    public static final String ORDER_QUEUE_NAME = "ORDER";

    // 支付 队列
    public static final String PAY_QUEUE_NAME ="payNotify";


}
