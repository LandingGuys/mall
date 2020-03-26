package com.henu.mall.service.member.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.mapper.ShippingMapper;
import com.henu.mall.pojo.Shipping;
import com.henu.mall.pojo.ShippingExample;
import com.henu.mall.request.ShippingRequest;
import com.henu.mall.service.member.ShippingService;
import com.henu.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lv
 * @date 2020-02-13 8:53
 */
@Service
public class ShippingServiceImpl implements ShippingService {
    @Resource
    private ShippingMapper shippingMapper;

    /**
     * 新增收货地址
     *
     * @param uid
     * @param request
     * @return
     */
    @Override
    public ResponseVo<Map<String, Integer>> add(Integer uid, ShippingRequest request) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(request,shipping);
        shipping.setUserId(uid);
        int i = shippingMapper.insertSelective(shipping);
        if(i == 0){
            ResponseVo.error(ResponseEnum.ERROR);
        }
        Map<String,Integer> map =new HashMap<>();
        map.put("shippingId",shipping.getId());
        return ResponseVo.success(map);
    }

    /**
     * 删除收货地址
     *
     * @param uid
     * @param shippingId
     * @return
     */
    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        ShippingExample example = new ShippingExample();
        example.createCriteria().andIdEqualTo(shippingId)
                .andUserIdEqualTo(uid);
        int i = shippingMapper.deleteByExample(example);
        if(i == 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 更新收货地址
     *
     * @param uid
     * @param shippingId
     * @param request
     * @return
     */
    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingRequest request) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(request,shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int i = shippingMapper.updateByPrimaryKeySelective(shipping);
        if( i == 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 收货地址分页列表查询
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        ShippingExample example = new ShippingExample();
        example.createCriteria().andUserIdEqualTo(uid);
        List<Shipping> shippings = shippingMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(shippings);
        return ResponseVo.success(pageInfo);
    }
}
