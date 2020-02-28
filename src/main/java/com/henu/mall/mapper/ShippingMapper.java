package com.henu.mall.mapper;

import com.henu.mall.pojo.Shipping;
import com.henu.mall.pojo.ShippingExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
@Mapper
public interface ShippingMapper {
    long countByExample(ShippingExample example);

    int deleteByExample(ShippingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    List<Shipping> selectByExampleWithRowbounds(ShippingExample example, RowBounds rowBounds);

    List<Shipping> selectByExample(ShippingExample example);

    Shipping selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Shipping record, @Param("example") ShippingExample example);

    int updateByExample(@Param("record") Shipping record, @Param("example") ShippingExample example);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
}