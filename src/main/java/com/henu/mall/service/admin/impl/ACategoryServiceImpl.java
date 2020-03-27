package com.henu.mall.service.admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.enums.CategorySearchTypeEnum;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.mapper.CategoryExtMapper;
import com.henu.mall.mapper.CategoryMapper;
import com.henu.mall.pojo.Category;
import com.henu.mall.request.CategoryAddRequest;
import com.henu.mall.request.CategoryUpdateRequest;
import com.henu.mall.service.admin.ACategoryService;
import com.henu.mall.service.member.CategoryService;
import com.henu.mall.vo.CategoryAdminVo;
import com.henu.mall.vo.CategoryVO;
import com.henu.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lv
 * @date 2020-03-26 10:47
 */
@Service
public class ACategoryServiceImpl implements ACategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryExtMapper categoryExtMapper;

    @Resource
    private CategoryService categoryService;

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

    /**
     * 管理员查询所有类目
     * @return
     */
    @Override
    public ResponseVo<List<CategoryAdminVo>> adminSelect() {
        
        ResponseVo<List<CategoryVO>> listResponseVo = categoryService.searchAll(CategorySearchTypeEnum.ADMIN.getType());
        List<CategoryAdminVo> categoryAdminVos = find(listResponseVo.getData());
        return ResponseVo.success(categoryAdminVos);
    }

    @Override
    public ResponseVo adminSelectAll(Integer categoryId,Integer pageNum,Integer pageSize,String query) {
        PageHelper.startPage(pageNum,pageSize);
        Integer type = CategorySearchTypeEnum.ADMIN.getType();
        List<Category> categories = categoryExtMapper.selectAll(type,query,categoryId);
        PageInfo pageInfo = new PageInfo<>(categories);
        pageInfo.setList(categories);
        return ResponseVo.success(pageInfo);
    }

    private List<CategoryAdminVo> find(List<CategoryVO> categoryVOList) {
        List<CategoryAdminVo> categoryAdminVoList = categoryVOList.stream().map(e -> {
            CategoryAdminVo categoryAdminVoChildren = new CategoryAdminVo();
            categoryAdminVoChildren.setLabel(e.getName());
            categoryAdminVoChildren.setValue(e.getId());
            categoryAdminVoChildren.setChildren(find(e.getSubCategories()));
            return categoryAdminVoChildren;
        }).collect(Collectors.toList());
        return categoryAdminVoList;
    }
}
