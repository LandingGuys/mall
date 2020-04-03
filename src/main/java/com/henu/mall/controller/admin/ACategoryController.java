package com.henu.mall.controller.admin;

import com.henu.mall.request.CategoryAddRequest;
import com.henu.mall.request.CategoryUpdateRequest;
import com.henu.mall.service.admin.ACategoryService;
import com.henu.mall.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-03-26 11:01
 */
@RestController
@RequestMapping("/admin")
@Api(description = "后台类目服务接口")
public class ACategoryController {
    @Resource
    private ACategoryService aCategoryService;

    @ApiOperation("获取所有类目树型")
    @GetMapping("/categories")
    public ResponseVo adminSelect(){
        return aCategoryService.adminSelect();
    }

    @ApiOperation("获取所有类目Table型")
    @GetMapping("/categoriesAll")
    public ResponseVo adminSelectAll(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String query){
        return aCategoryService.adminSelectAll(categoryId,pageNum,pageSize,query);
    }

    @ApiOperation("更新类目")
    @PutMapping("/categories")
    public ResponseVo update(@RequestBody CategoryUpdateRequest request){
        return aCategoryService.update(request);
    }

    @ApiOperation("新增类目")
    @PostMapping("/categories")
    public ResponseVo add(@RequestBody CategoryAddRequest request){
        return aCategoryService.add(request);
    }
}
