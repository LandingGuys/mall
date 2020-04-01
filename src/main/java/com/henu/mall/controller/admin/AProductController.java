package com.henu.mall.controller.admin;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.ProductAddRequest;
import com.henu.mall.request.ProductUpdateRequest;
import com.henu.mall.service.admin.AProductService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-03-26 11:02
 */
@RestController
@RequestMapping("/admin")
public class AProductController {
    @Resource
    private AProductService aProductService;

    @AuthIgnore
    @GetMapping("/products")
    public ResponseVo list(@RequestParam(required = false) Integer categoryId,
                           @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                           @RequestParam(required = false) String query
                           ){
        return aProductService.list(categoryId,pageNum,pageSize,query);
    }
    @GetMapping("/products/{productId}")
    public ResponseVo detail(@PathVariable Integer productId){
        return aProductService.detail(productId);
    }
    @AuthIgnore
    @PostMapping("/products")
    public ResponseVo add(@RequestBody ProductAddRequest request){
        return aProductService.add(request);
    }
    @AuthIgnore
    @DeleteMapping("/products/{productId}")
    public  ResponseVo delete(@PathVariable Integer productId){
        return aProductService.delete(productId);
    }
    @AuthIgnore
    @PutMapping("/products")
    public ResponseVo update(@RequestBody ProductUpdateRequest request){
        return aProductService.update(request);
    }

}
