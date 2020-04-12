package com.henu.mall.controller.merber;

import com.github.pagehelper.PageInfo;
import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.OrderCreateRequest;
import com.henu.mall.service.member.OrderService;
import com.henu.mall.vo.OrderVo;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author lv
 * @date 2020-02-19 22:25
 */
@RestController
@AuthIgnore
@Api(description = "前台订单服务接口")
public class OrderController {
    @Resource
    private OrderService orderService;

    @ApiOperation("创建订单")
    @AuthIgnore
    @PostMapping("/user/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateRequest orderCreateRequest,
                                       HttpServletRequest request){
        UserVo user =(UserVo) request.getSession().getAttribute("user");

        return orderService.create(user.getId(),orderCreateRequest,request);
    }
    @ApiOperation("获取当前用户订单列表")
    @AuthIgnore
    @GetMapping("/user/orders")
    public ResponseVo<PageInfo> list( @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                      @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                     HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.list(user.getId(),pageNum,pageSize);

    }

    @ApiOperation("根据订单id获取当前用户订单详情")
    @AuthIgnore
    @GetMapping("/user/orderDetail/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable("orderNo") Long orderNo,
                                      HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.detail(user.getId(),orderNo);

    }
    @ApiOperation("根据订单id确认收货")
    @AuthIgnore
    @PutMapping(value = "/user/orderReceipt/{orderNo}")
    public ResponseVo receipt(@PathVariable("orderNo") Long orderNo,
                             HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        String operation = "receipt";
        return orderService.update(user.getId(),orderNo,operation);
    }
    @ApiOperation("根据订单id确认完成订单")
    @AuthIgnore
    @PutMapping(value = "/user/orderFinish/{orderNo}")
    public ResponseVo finish(@PathVariable("orderNo") Long orderNo,
                              HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        String operation = "finish";
        return orderService.update(user.getId(),orderNo,operation);
    }

    @ApiOperation("根据订单id取消订单")
    @AuthIgnore
    @PutMapping("/user/orderDetail/{orderNo}")
    public ResponseVo cancel(@PathVariable("orderNo") Long orderNo,
                             HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.cancel(user.getId(),orderNo);
    }

    @DeleteMapping("/user/order/{orderNo}")
    @AuthIgnore
    @ApiOperation("根据订单id删除订单")
    public ResponseVo delete(@PathVariable("orderNo") Long orderNo,
                             HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.delete(user.getId(),orderNo);
    }
}
