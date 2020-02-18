package com.henu.mall.mapper;

import com.henu.mall.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author lv
 * @date 2020-02-02 9:23
 */
@Mapper
public interface ProductExtMapper {

    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    List<Product> selectByProductIdSet(@Param("productIdSet") Set<Integer> productIdSet);
}
