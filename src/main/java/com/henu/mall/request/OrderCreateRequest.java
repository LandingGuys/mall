package com.henu.mall.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lv
 * @date 2020-02-19 22:29
 */
@Data
public class OrderCreateRequest {


    @NotBlank(message ="收货人不能为空")
    private String receiverName;

    @NotBlank(message ="收货人联系方式不能为空")
    private String receiverPhone;

    @NotBlank(message ="收货人地址不能为空")
    private String receiverAddress;

}
