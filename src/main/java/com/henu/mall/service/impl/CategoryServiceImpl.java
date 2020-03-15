package com.henu.mall.service.impl;

import com.henu.mall.consts.MallConsts;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.mapper.CategoryExtMapper;
import com.henu.mall.mapper.CategoryMapper;
import com.henu.mall.pojo.Category;
import com.henu.mall.request.CategoryAddRequest;
import com.henu.mall.request.CategoryUpdateRequest;
import com.henu.mall.service.CategoryService;
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
    private CategoryMapper categoryMapper;
    @Resource
    private CategoryExtMapper categoryExtMapper;

    @Override
    public ResponseVo<List<CategoryVO>> searchAll() {
        //查出所有类目数据
        List<Category> categories = categoryExtMapper.selectAll();
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
     * 查询子类目id
     *
     * @param id
     * @param resultSet
     */
    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryExtMapper.selectAll();
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


    /**
     * 新增类目
     */
    @Override
    public ResponseVo add(CategoryAddRequest request) {
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        int row = categoryMapper.insertSelective(category);
        if (row <= 0) {
            return ResponseVo.error(ResponseEnum.CATEGORY_ADD_ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 更新类目
     *
     * @param request
     * @return
     */
    @Override
    public ResponseVo update(CategoryUpdateRequest request) {
        Category categoryById = categoryMapper.selectByPrimaryKey(request.getCategoryId());
        if (categoryById == null) {
            return ResponseVo.error(ResponseEnum.CATEGORY_NOT_EXIST);
        }
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        int row = categoryMapper.updateByPrimaryKeySelective(category);

        if (row <= 0) {
            return ResponseVo.error(ResponseEnum.CATEGORY_UPDATE_ERROR);
        }
        return ResponseVo.success();
    }

}
