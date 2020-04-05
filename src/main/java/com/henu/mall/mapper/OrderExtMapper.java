package com.henu.mall.mapper;

import com.henu.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author lv
 * @date 2020-04-04 15:06
 */
public interface OrderExtMapper {

    List<Order> selectAll(@Param("orderNo") Long orderNo, @Param("receiverNameOrPhone") String receiverNameOrPhone, @Param("time") Date time);
}
