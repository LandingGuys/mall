package com.henu.mall.mapper;

import com.henu.mall.pojo.Product;
import com.henu.mall.request.ProductSelectCondition;
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
    /**
     * 通过categoryId 查询、搜索商品 (包括在售、下架）
     * @param categoryIdSet
     * @return
     */
    List<Product> selectAllByCondition(@Param("categoryIdSet") Set<Integer> categoryIdSet,@Param("query") String query);

    /**
     * 通过categoryId 查询在售商品
     * @param categoryIdSet
     * @return
     */
    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    List<Product> selectByProductIdSet(@Param("productIdSet") Set<Integer> productIdSet);

    /**
     * 只搜索在售
     * @param condition
     * @return
     */
    List<Product> selectByCondition(ProductSelectCondition condition);


    List<Product> selectAllByType(@Param("type") String type);
}
