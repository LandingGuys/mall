package com.henu.mall.controller.admin;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.ProductAddRequest;
import com.henu.mall.request.ProductUpdateRequest;
import com.henu.mall.service.admin.AProductService;
import com.henu.mall.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-03-26 11:02
 */
@Api(description = "管理员商品服务接口")
@RestController
@RequestMapping("/admin")
public class AProductController {
    @Resource
    private AProductService aProductService;

    @ApiOperation("获取商品列表")
    @AuthIgnore
    @GetMapping("/products")
    public ResponseVo list(@RequestParam(required = false) Integer categoryId,
                           @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                           @RequestParam(required = false) String query
                           ){
        return aProductService.list(categoryId,pageNum,pageSize,query);
    }

    @ApiOperation("根据商品id获取商品详细信息")
    @GetMapping("/products/{productId}")
    public ResponseVo detail(@PathVariable Integer productId){
        return aProductService.detail(productId);
    }

    @ApiOperation("新增商品")
    @AuthIgnore
    @PostMapping("/products")
    public ResponseVo add(@RequestBody ProductAddRequest request){
        return aProductService.add(request);
    }

    @ApiOperation("根据商品id删除商品")
    @AuthIgnore
    @DeleteMapping("/products/{productId}")
    public  ResponseVo delete(@PathVariable Integer productId){
        return aProductService.delete(productId);
    }

    @ApiOperation("根据商品id更新商品")
    @AuthIgnore
    @PutMapping("/products")
    public ResponseVo update(@RequestBody ProductUpdateRequest request){
        return aProductService.update(request);
    }

}
