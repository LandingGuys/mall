package com.henu.mall.mapper;

import com.henu.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author lv
 * @date 2020-02-15 19:37
 */
@Mapper
public interface ShippingExtMapper {

    List<Shipping> selectByIdSet(@Param("shippingIdSet") Set<Integer> shippingIdSet);
}
