package com.henu.mall.controller.admin;

import com.henu.mall.service.admin.ACategoryService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-03-26 11:01
 */
@RestController
@RequestMapping("/admin")
public class ACategoryController {
    @Resource
    private ACategoryService aCategoryService;
    @GetMapping("/categories")
    public ResponseVo adminSelectAll(){
        return aCategoryService.adminSelectAll();
    }
}