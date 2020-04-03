package com.henu.mall.controller.merber;

import com.henu.mall.request.ProductSelectCondition;
import com.henu.mall.service.member.ProductService;
import com.henu.mall.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-02-02 9:43
 */
@Api(description = "前台商品服务接口")
@RestController
public class ProductController {

    @Resource
    private ProductService productService;

    @ApiOperation("获取商品列表")
    @GetMapping("/products")
    public ResponseVo list(@RequestParam(required = false) Integer categoryId,
                           @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return productService.list(categoryId,pageNum,pageSize);
    }

    @ApiOperation("根据type获取商品")
    @GetMapping("/products/type")
    public ResponseVo hotOrNew(@RequestParam String type){
        return productService.hotOrNew(type);
    }

    @ApiOperation("获取商品详情")
    @GetMapping("/products/{productId}")
    public ResponseVo detail(@PathVariable Integer productId){
        return productService.detail(productId);
    }

    @ApiOperation("根据关键词搜索商品")
    @GetMapping("/products/search")
    public ResponseVo select(ProductSelectCondition condition){
        return productService.getProductListByCondition(condition);
    }

}
