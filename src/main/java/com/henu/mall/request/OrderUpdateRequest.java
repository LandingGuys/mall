package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-04-04 15:01
 */
@Data
public class OrderUpdateRequest {

    private Long orderNo;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;


}
