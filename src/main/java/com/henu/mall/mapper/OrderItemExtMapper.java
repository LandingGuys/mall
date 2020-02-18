package com.henu.mall.mapper;

import com.henu.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author lv
 * @date 2020-02-15 11:06
 */
public interface OrderItemExtMapper {

    int batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

    List<OrderItem> selectByOrderNoSet(@Param("orderNoSet") Set<Long> orderNoSet);
}
