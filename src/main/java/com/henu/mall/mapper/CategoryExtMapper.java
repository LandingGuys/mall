package com.henu.mall.mapper;

import com.henu.mall.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lv
 * @date 2020-01-31 12:55
 */
@Mapper
public interface CategoryExtMapper {
    /**
     * 查询所有类目
     * @return
     */
   List<Category> selectAll(@Param("type") Integer type,@Param("query") String query,@Param("categoryId") Integer categoryId);
}
