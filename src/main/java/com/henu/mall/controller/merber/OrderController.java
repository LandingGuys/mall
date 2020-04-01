package com.henu.mall.controller.merber;

import com.github.pagehelper.PageInfo;
import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.OrderCreateRequest;
import com.henu.mall.service.member.OrderService;
import com.henu.mall.vo.OrderVo;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
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
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/user/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateRequest orderCreateRequest,
                                       HttpServletRequest request){
        UserVo user =(UserVo) request.getSession().getAttribute("user");

        return orderService.create(user.getId(),orderCreateRequest,request);
    }
    @GetMapping("/user/orders")
    public ResponseVo<PageInfo> list( @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                      @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                     HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.list(user.getId(),pageNum,pageSize);

    }

    @GetMapping("/user/orderDetail/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable("orderNo") Long orderNo,
                                      HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.detail(user.getId(),orderNo);

    }

    @PutMapping("/user/orderDetail/{orderNo}")
    public ResponseVo cancel(@PathVariable("orderNo") Long orderNo,
                             HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return orderService.cancel(user.getId(),orderNo);
    }
}
