package com.henu.mall.controller;

import com.github.pagehelper.PageInfo;
import com.henu.mall.pojo.User;
import com.henu.mall.request.OrderCreateRequest;
import com.henu.mall.service.OrderService;
import com.henu.mall.vo.OrderVo;
import com.henu.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author lv
 * @date 2020-02-19 22:25
 */
@RestController
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateRequest request,
                                      HttpSession session){
        User user = (User) session.getAttribute("user");
        return orderService.create(user.getId(),request.getShippingId());
    }
    @GetMapping("/orders")
    public ResponseVo<PageInfo> list( @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                      @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                     HttpSession session){
        User user = (User) session.getAttribute("user");
        return orderService.list(user.getId(),pageNum,pageSize);

    }

    @GetMapping("/orders/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable("orderNo") Long orderNo,
                                      HttpSession session){
        User user = (User) session.getAttribute("user");
        return orderService.detail(user.getId(),orderNo);

    }

    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable("orderNo") Long orderNo,
                             HttpSession session){
        User user = (User) session.getAttribute("user");
        return orderService.cancel(user.getId(),orderNo);
    }
}
