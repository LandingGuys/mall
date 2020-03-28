package com.henu.mall.service.member.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.consts.MQConstant;
import com.henu.mall.consts.MallConsts;
import com.henu.mall.enums.*;
import com.henu.mall.mapper.*;
import com.henu.mall.pojo.*;
import com.henu.mall.request.OrderCreateRequest;
import com.henu.mall.service.member.CartService;
import com.henu.mall.service.member.MessageService;
import com.henu.mall.service.member.OrderService;
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
    private CartService cartService;

    @Resource
    private ProductExtMapper productExtMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemExtMapper orderItemExtMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private MessageService messageService;

    @Resource
    private UserMapper userMapper;
    /**
     * 创建订单
     *
     * @param uid
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, OrderCreateRequest request) {

        //1.获取购物车，校验（是否有商品、库存）
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
        Order order = buildOrder(uid, orderNo, request, orderItemList);
        int rowForOrder = orderMapper.insertSelective(order);
        if(rowForOrder <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        int rowForOrderItem = orderItemExtMapper.batchInsert(orderItemList);
        if(rowForOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        //更新购物车（删除选中的商品） Redis 没有事务 不能回滚 所以确保前面没有发生异常
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }
        //构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList);
        // 设置订单超时时间 发消息到 mq (orderId) 设置过期时间 未在规定时间内完成支付，将自动取消订单
        messageService.send(MQConstant.ORDER_QUEUE_NAME,orderVo.getOrderNo().toString(), MallConsts.ORDER_TIME_OUT_TIME);

        return ResponseVo.success(orderVo);
    }
    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> OrderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(OrderItemVoList);
        return orderVo;
    }
    private Order buildOrder(Integer uid, Long orderNo, OrderCreateRequest request, List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
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
        User user = userMapper.selectByPrimaryKey(uid);
        PageHelper.startPage(pageNum,pageSize);
        OrderExample example = new OrderExample();

        //example.createCriteria().andUserIdEqualTo(uid);

        if(!user.getRole().equals(RoleEnum.ADMIN.getCode())){
            example.setOrderByClause("`create_time` DESC");
            example.createCriteria().andUserIdEqualTo(uid);
        }else{
            example.setOrderByClause("`create_time` DESC");
        }
        List<Order> orderList = orderMapper.selectByExample(example);
        Set<Long> orderNoSet = orderList.stream().map(Order::getOrderNo).collect(Collectors.toSet());

        List<OrderItem> orderItemList = orderItemExtMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItem::getOrderNo));

//        Set<Integer> shippingIdSet = orderList.stream().map(Order::getShippingId).collect(Collectors.toSet());
//        List<Shipping> shippingList = shippingExtMapper.selectByIdSet(shippingIdSet);
//        Map<Integer, Shipping> shippingMap = shippingList.stream().collect(Collectors.toMap(Shipping::getId, shipping -> shipping));
        List<OrderVo> orderVoList =new ArrayList<>();

        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order, orderItemMap.get(order.getOrderNo()));
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
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(orderNo);
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        User user = userMapper.selectByPrimaryKey(uid);
        if(user.getRole().equals(RoleEnum.ADMIN.getCode())){
            if(CollectionUtils.isEmpty(orderList)){
                return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
            }
        }else{
            if(CollectionUtils.isEmpty(orderList) || !orderList.get(0).getUserId().equals(uid)){
                return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
            }
        }
        OrderItemExample example = new OrderItemExample();
        if(user.getRole().equals(RoleEnum.ADMIN.getCode())){
            example.createCriteria().andOrderNoEqualTo(orderNo);
        }else{
            example.createCriteria().andOrderNoEqualTo(orderNo)
                    .andUserIdEqualTo(uid);
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByExample(example);
        OrderVo orderVo = buildOrderVo(orderList.get(0), orderItemList);
        return ResponseVo.success(orderVo);
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
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(orderNo);
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        User user = userMapper.selectByPrimaryKey(uid);
        if(user.getRole().equals(RoleEnum.ADMIN.getCode())){
            if(CollectionUtils.isEmpty(orderList)){
                return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
            }
        }else{
            if (CollectionUtils.isEmpty(orderList) || !orderList.get(0).getUserId().equals(uid)) {
                return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
            }
        }
        Order order = orderList.get(0);
        //只有[未付款]订单可以取消，看自己公司业务
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();
    }

    /**
     * 支付后修改订单状态
     *
     * @param orderNo
     */
    @Override
    public void paid(Long orderNo) {
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(orderNo);
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        if (CollectionUtils.isEmpty(orderList)) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单id:" + orderNo);
        }
        Order order = orderList.get(0);
        //只有[未付款]订单可以变成[已付款]，看自己公司业务
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc()+"订单id:" + orderNo);
        }
        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            throw new RuntimeException("将订单更新为已支付状态失败，订单id:" + orderNo);
        }

    }

    /**
     * 超时自动取消订单
     * @param orderNo
     */
    @Override
    public void cancel(Long orderNo) {
        //1.判断订单是否存在
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<Order> orderList = orderMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(orderList)){
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单id:" + orderNo);
        }
        //2.判断订单是否未支付并且订单未取消
        Order order = orderList.get(0);
        if(order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode()) && !order.getStatus().equals(OrderStatusEnum.CANCELED)){
            //3.将未支付并且未取消的订单取消
            order.setStatus(OrderStatusEnum.CANCELED.getCode());
            int row = orderMapper.updateByPrimaryKeySelective(order);
            if(row <= 0){
                throw new RuntimeException("将超时未支付订单自动取消失败，订单id:" + orderNo);
            }
        }
    }
}
