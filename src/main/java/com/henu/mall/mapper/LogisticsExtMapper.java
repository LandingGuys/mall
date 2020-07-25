package com.henu.mall.mapper;


import com.henu.mall.pojo.Logistics;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lv
 * @date 2020-04-10 16:45
 */
@Mapper
public interface LogisticsExtMapper {

    int insertSelective(Logistics record);

    Logistics get(Long orderNo);
}
