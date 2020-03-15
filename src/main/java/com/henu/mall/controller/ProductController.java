package com.henu.mall.controller;

import com.henu.mall.request.ProductAddRequest;
import com.henu.mall.request.ProductSelectCondition;
import com.henu.mall.request.ProductUpdateRequest;
import com.henu.mall.service.ProductService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/products")
    public ResponseVo add(@RequestBody ProductAddRequest request){
        return productService.add(request);
    }
    @DeleteMapping("/products")
    public  ResponseVo delete(@RequestParam Integer productId){
        return productService.delete(productId);
    }
    @PutMapping("/products")
    public ResponseVo update(@RequestBody ProductUpdateRequest request){
        return productService.update(request);
    }
    @GetMapping("/products/search")
    public ResponseVo select(ProductSelectCondition condition){
        return productService.getProductListByCondition(condition);
    }



}
