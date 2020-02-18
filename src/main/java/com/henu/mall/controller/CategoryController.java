package com.henu.mall.controller;

import com.henu.mall.service.CategoryService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-01-31 13:12
 */
@RestController
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @GetMapping("/categories")
    public ResponseVo selectAll(){
        return categoryService.searchAll();
    }
}
