package com.henu.mall.request;

import lombok.Data;

/**
 * @author lv
 * @date 2020-02-13 8:33
 */
@Data
public class ShippingRequest {

    //收货人
    private String receiverName;
    //收货固定电话
    private String receiverPhone;
    //收货手机
    private String receiverMobile;
    //收货城市
    private String receiverCity;
    //
    private String receiverDistrict;
    //收货详细地址
    private String receiverAddress;
    //收货邮编
    private String receiverZip;
}
