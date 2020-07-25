package com.henu.mall.request;

import com.henu.mall.pojo.Cart;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lv
 * @date 2020-02-19 22:29
 */
@Data
public class OrderCreateRequest {

    @NotNull
    private Integer type;

    @NotBlank(message ="收货人不能为空")
    private String receiverName;

    @NotBlank(message ="收货人联系方式不能为空")
    private String receiverPhone;

    @NotBlank(message ="收货人地址不能为空")
    private String receiverAddress;

    @NotNull
    private List<Cart> orderProductList;

}
