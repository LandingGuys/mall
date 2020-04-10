package com.henu.mall.controller.admin;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.LogisticsAddRequest;
import com.henu.mall.service.admin.TransportationService;
import com.henu.mall.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-04-10 16:40
 */
@RestController
@RequestMapping("/admin")
@Api(description = "管理员物流接口服务")
public class TransportationController {

    @Resource
    private TransportationService transportationService;

    @ApiOperation("添加物流信息")
    @AuthIgnore
    @PostMapping("/transportation")
    public ResponseVo add(@RequestBody LogisticsAddRequest request){
        return transportationService.add(request);
    }
}
