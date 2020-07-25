package com.henu.mall.service.member.impl;

import com.henu.mall.consts.MallConsts;
import com.henu.mall.enums.CategorySearchTypeEnum;
import com.henu.mall.mapper.CategoryExtMapper;
import com.henu.mall.pojo.Category;
import com.henu.mall.service.member.CategoryService;
import com.henu.mall.vo.CategoryVO;
import com.henu.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lv
 * @date 2020-01-28 14:42
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryExtMapper categoryExtMapper;

    @Override
    public ResponseVo<List<CategoryVO>> searchAll(Integer type) {
        //查出所有类目数据

        List<Category> categories = categoryExtMapper.selectAll(type,null,null);
        //筛选出根目录
        List<CategoryVO> categoryVOList = categories.stream()
                .filter(e -> e.getParentId().equals(MallConsts.ROOT_PARENT_ID))
                .map(this::category2CategoryVO)
                .sorted(Comparator.comparing(CategoryVO::getSortOrder).reversed())
                .collect(Collectors.toList());
        //查子目录
        findSubCategories(categoryVOList, categories);

        return ResponseVo.success(categoryVOList);
    }

    /**
     * 查询子类目
     *
     * @param categoryVOList
     * @param categories
     */
    private void findSubCategories(List<CategoryVO> categoryVOList, List<Category> categories) {
        for (CategoryVO categoryVO : categoryVOList) {
            List<CategoryVO> subCategoryVoList = new ArrayList<>();

            for (Category category : categories) {
                //如果查到内容，设置subCategory,继续往下查
                if (categoryVO.getId().equals(category.getParentId())) {
                    CategoryVO subCategoryVo = category2CategoryVO(category);
                    subCategoryVoList.add(subCategoryVo);
                }

                subCategoryVoList.sort(Comparator.comparing(CategoryVO::getSortOrder).reversed());
                categoryVO.setSubCategories(subCategoryVoList);
                //递归
                findSubCategories(subCategoryVoList, categories);
            }
        }

    }

    /**
     * 转换对象
     *
     * @param category
     * @return
     */
    private CategoryVO category2CategoryVO(Category category) {
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        return categoryVO;
    }

    /**
     * 查询子类目id 供前台商品模块使用
     *
     * @param id
     * @param resultSet
     */
    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        Integer type = CategorySearchTypeEnum.MEMBER.getType();
        List<Category> categories = categoryExtMapper.selectAll(type,null,null);
        findSubCategoryId(id, resultSet, categories);
    }

    /**
     * 递归查询子类目id
     *
     * @param id
     * @param resultSet
     * @param categories
     */
    public void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }





}
