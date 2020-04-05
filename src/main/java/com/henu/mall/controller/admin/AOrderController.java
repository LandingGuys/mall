package com.henu.mall.controller.admin;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.service.admin.AOrderService;
import com.henu.mall.service.member.OrderService;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author lv
 * @date 2020-04-04 12:29
 */
@RestController
@RequestMapping(value = "/admin")
@Api(description = "后台订单服务接口")
public class AOrderController {

    @Resource
    private AOrderService aOrderService;

    @Resource
    private OrderService orderService;

    @ApiOperation("获取订单列表")
    @AuthIgnore
    @GetMapping(value = "/orders")
    public ResponseVo list(@RequestParam(required = false) Long orderNo,
                           @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                           @RequestParam(required = false) String receiverNameOrPhone,
                           @RequestParam(required = false) Date time){

        return aOrderService.list(orderNo,pageNum,pageSize,receiverNameOrPhone,time);
    }

    @ApiOperation("根据订单id获取订单详情")
    @AuthIgnore
    @GetMapping(value = "/orders/{orderNo}")
    public ResponseVo detail(@PathVariable("orderNo") Long orderNo,
                             HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.detail(user.getId(),orderNo);
    }

    @ApiOperation("根据订单id取消订单")
    @AuthIgnore
    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable("orderNo") Long orderNo,
                             HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.cancel(user.getId(),orderNo);
    }

    @DeleteMapping("/orders/{orderNo}")
    @AuthIgnore
    @ApiOperation("根据订单id删除订单")
    public ResponseVo delete(@PathVariable("orderNo") Long orderNo,
                             HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.delete(user.getId(),orderNo);
    }

}
