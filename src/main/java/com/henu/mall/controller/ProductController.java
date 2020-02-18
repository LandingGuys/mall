package com.henu.mall.controller;

import com.henu.mall.service.ProductService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-02-02 9:43
 */
@RestController
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/products")
    public ResponseVo list(@RequestParam(required = false) Integer categoryId,
                           @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return productService.list(categoryId,pageNum,pageSize);
    }

    @GetMapping("/products/{productId}")
    public ResponseVo detail(@PathVariable Integer productId){
        return productService.detail(productId);
    }

}
