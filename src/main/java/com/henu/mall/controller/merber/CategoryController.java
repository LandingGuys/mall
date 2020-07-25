package com.henu.mall.controller.merber;

import com.henu.mall.enums.CategorySearchTypeEnum;
import com.henu.mall.service.member.CategoryService;
import com.henu.mall.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-01-31 13:12
 */
@Api(description = "前台类目服务接口")
@RestController
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @ApiOperation("获取所有类目未下架")
    @GetMapping("/categories")
    public ResponseVo selectAll(){
        return categoryService.searchAll(CategorySearchTypeEnum.MEMBER.getType());
    }

}
