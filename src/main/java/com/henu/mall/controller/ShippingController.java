package com.henu.mall.controller;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.ShippingRequest;
import com.henu.mall.service.ShippingService;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author lv
 * @date 2020-02-20 17:13
 */
@RestController
@AuthIgnore
public class ShippingController {
    @Resource
    private ShippingService shippingService;

    @PostMapping("/shippings")
    public ResponseVo add (@Valid @RequestBody ShippingRequest shippingRequest, HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return shippingService.add(user.getId(),shippingRequest);
    }
    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId,
                             HttpSession session) {
        UserVo user =(UserVo) session.getAttribute("user");
        return shippingService.delete(user.getId(), shippingId);
    }

    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingRequest form,
                             HttpSession session) {
        UserVo user =(UserVo) session.getAttribute("user");
        return shippingService.update(user.getId(), shippingId, form);
    }

    @GetMapping("/shippings")
    public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           HttpSession session) {
        UserVo user =(UserVo) session.getAttribute("user");
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
