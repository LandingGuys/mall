package com.henu.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.enums.OrderStatusEnum;
import com.henu.mall.enums.PaymentTypeEnum;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.SaleEnum;
import com.henu.mall.mapper.*;
import com.henu.mall.pojo.*;
import com.henu.mall.service.CartService;
import com.henu.mall.service.OrderService;
import com.henu.mall.vo.OrderItemVo;
import com.henu.mall.vo.OrderVo;
import com.henu.mall.vo.ResponseVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lv
 * @date 2020-02-13 16:29
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private ShippingMapper shippingMapper;

    @Resource
    private ShippingExtMapper shippingExtMapper;

    @Resource
    private CartService cartService;

    @Resource
    private ProductExtMapper productExtMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemExtMapper orderItemExtMapper;
    /**
     * 创建订单
     *
     * @param uid
     * @param shippingId
     * @return
     */
    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //1.收货地质校验（后面也会需要收货地址）
        ShippingExample example = new ShippingExample();
        example.createCriteria().andUserIdEqualTo(uid)
                .andIdEqualTo(shippingId);

        List<Shipping> shippings = shippingMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(shippings)){
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }
        //2.获取购物车，校验（是否有商品、库存）
        List<Cart> cartList = cartService.listForCart(uid).stream().filter(
                Cart::getProductSelected
        ).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(cartList)){
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }
        //查购物车商品
        Set<Integer> productIdSets = cartList.stream().map(Cart::getProductId).collect(Collectors.toSet());
        List<Product> productList = productExtMapper.selectByProductIdSet(productIdSets);
        Map<Integer, Product> map = productList.stream().collect(Collectors.toMap(Product::getId, product -> product));
        //唯一订单id
        Long orderNo = generateOrderNo();
        //订单详情表 一对多， order 对 多orderItem
        List<OrderItem> orderItemList =new ArrayList<>();
        for (Cart cart : cartList) {
            Product product = map.get(cart.getProductId());
            //数据库商品不存在
            if(product == null){
               return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,"商品不存在，productId ="+cart.getProductId());
            }
            //商品上下架状态
            if(!product.getStatus().equals(SaleEnum.ON_SALE.getStatus())){
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE,"商品不是在售状态"+product.getName());
            }
            //库存是否充足
            if(product.getStock() < cart.getQuantity()){
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR,"库存不正确 "+product.getName());
            }
            //减库存
            product.setStock(product.getStock() -cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if(row <= 0){
                return ResponseVo.error(ResponseEnum.ERROR);
            }
            //计算总价 只计算被选中的
            //生成订单，入库 OrderItem ,事务

            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);
        }
        //计算总价 只计算被选中的
        //生成订单，入库 Order ,事务
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);
        int rowForOrder = orderMapper.insertSelective(order);
        if(rowForOrder <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        int rowForOrderItem = orderItemExtMapper.batchInsert(orderItemList);
        if(rowForOrderItem <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        
        //更新购物车（删除选中的商品） Redis 没有事务 不能回滚 所以确保前面没有发生异常
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }
        //构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shippings.get(0));
        return ResponseVo.success(orderVo);
    }

    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> OrderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(OrderItemVoList);

        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }

    private Order buildOrder(Integer uid, Long orderNo, Integer shippingId, List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        return order;
    }


    /**
     * 构建OrderItem 对象
     * @param uid
     * @param orderNo
     * @param quantity
     * @param product
     * @return
     */
    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
    //orderNo 生成器
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }


    /**
     * 订单列表
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        OrderExample example = new OrderExample();
        example.createCriteria().andUserIdEqualTo(uid);
        List<Order> orderList = orderMapper.selectByExample(example);
        Set<Long> orderNoSet = orderList.stream().map(Order::getOrderNo).collect(Collectors.toSet());

        List<OrderItem> orderItemList = orderItemExtMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItem::getOrderNo));

        Set<Integer> shippingIdSet = orderList.stream().map(Order::getShippingId).collect(Collectors.toSet());
        List<Shipping> shippingList = shippingExtMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = shippingList.stream().collect(Collectors.toMap(Shipping::getId, shipping -> shipping));
        List<OrderVo> orderVoList =new ArrayList<>();

        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order, orderItemMap.get(order.getOrderNo()), shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVoList);
        return ResponseVo.success(pageInfo);
    }

    /**
     * 订单详情
     *
     * @param uid
     * @param orderNo
     * @return
     */
    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        return null;
    }

    /**
     * 取消订单
     *
     * @param uid
     * @param orderNo
     * @return
     */
    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        return null;
    }
}
