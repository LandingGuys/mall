package com.henu.mall.controller.merber;

import com.henu.mall.annotation.AuthIgnore;
import com.henu.mall.request.ShippingRequest;
import com.henu.mall.service.member.ShippingService;
import com.henu.mall.vo.ResponseVo;
import com.henu.mall.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author lv
 * @date 2020-02-20 17:13
 */
@Api(description = "前台收货地址服务接口")
@RestController
@AuthIgnore
public class ShippingController {
    @Resource
    private ShippingService shippingService;

    @ApiOperation("添加收货地址")
    @AuthIgnore
    @PostMapping("/shippings")
    public ResponseVo add (@Valid @RequestBody ShippingRequest shippingRequest, HttpSession session){
        UserVo user =(UserVo) session.getAttribute("user");
        return shippingService.add(user.getId(),shippingRequest);
    }

    @ApiOperation("删除收货地址")
    @AuthIgnore
    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId,
                             HttpSession session) {
        UserVo user =(UserVo) session.getAttribute("user");
        return shippingService.delete(user.getId(), shippingId);
    }

    @ApiOperation("更新收货地址")
    @AuthIgnore
    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingRequest form,
                             HttpSession session) {
        UserVo user =(UserVo) session.getAttribute("user");
        return shippingService.update(user.getId(), shippingId, form);
    }

    @ApiOperation("获取收货地址列表")
    @AuthIgnore
    @GetMapping("/shippings")
    public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           HttpSession session) {
        UserVo user =(UserVo) session.getAttribute("user");
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
