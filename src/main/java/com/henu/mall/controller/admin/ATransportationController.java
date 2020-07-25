package com.henu.mall.controller.admin;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.CourierSelectRequest;
import com.henu.mall.request.LogisticsAddRequest;
import com.henu.mall.service.admin.TransportationService;
import com.henu.mall.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lv
 * @date 2020-04-10 16:40
 */
@RestController
@Api(description = "管理员物流接口服务")
public class ATransportationController {

    @Resource
    private TransportationService transportationService;

    @ApiOperation("添加物流信息")
    @AuthIgnore
    @PostMapping("/admin/transportation")
    public ResponseVo add(@RequestBody LogisticsAddRequest request){
        return transportationService.add(request);
    }

    @ApiOperation("获取订单物流信息")
    @AuthIgnore
    @GetMapping("/transportation")
    public ResponseVo get(@RequestParam Long orderNo){
        return transportationService.get(orderNo);
    }

    @PostMapping("/transportation/track")
    @AuthIgnore
    @ApiOperation("获取物流轨迹")
    public ResponseVo getTrack(@RequestBody CourierSelectRequest request){
        return transportationService.getTrack(request.getExpCode(),request.getExpNo());
    }
}
