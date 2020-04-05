package com.henu.mall.service.member.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.consts.MQConstant;
import com.henu.mall.consts.MallConsts;
import com.henu.mall.enums.*;
import com.henu.mall.mapper.*;
import com.henu.mall.pojo.*;
import com.henu.mall.request.OrderCreateRequest;
import com.henu.mall.service.member.MessageService;
import com.henu.mall.service.member.OrderService;
import com.henu.mall.utils.IPInfoUtil;
import com.henu.mall.utils.RedisUtil;
import com.henu.mall.vo.OrderItemVo;
import com.henu.mall.vo.OrderVo;
import com.henu.mall.vo.ResponseVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lv
 * @date 2020-02-13 16:29
 */
@Service
public class OrderServiceImpl implements OrderService {


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

    @Resource
    private RedisUtil redisUtil;
    /**
     * 创建订单
     *
     * @param uid
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, OrderCreateRequest orderCreateRequest, HttpServletRequest request) {
        // 1.判断请求信息
        String receiverName = orderCreateRequest.getReceiverName();
        String receiverPhone = orderCreateRequest.getReceiverPhone();
        String receiverAddress = orderCreateRequest.getReceiverAddress();
        List<Cart> orderProduct = orderCreateRequest.getOrderProductList();
        if(StringUtils.isBlank(receiverName) || StringUtils.isBlank(receiverPhone)
            || StringUtils.isBlank(receiverAddress) || CollectionUtils.isEmpty(orderProduct)){
            return ResponseVo.error(ResponseEnum.REQUEST_MSG_ERROR);
        }

        //2. 判断用户是否存在
        User user = userMapper.selectByPrimaryKey(uid);
        if(user == null){
            return ResponseVo.error(ResponseEnum.USER_NOT_EXIST);
        }

        // 请求 ip 地址
        String ip = IPInfoUtil.getIpAddr(request);
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip="127.0.0.1";
        }

        // Redis key，防止恶意请求
        String orderKey = String.format(MallConsts.ORDER_KEY_TEMPLATE,ip);
        String temp = redisUtil.get(orderKey);
        if (StringUtils.isNotBlank(temp)) {
            return ResponseVo.error(ResponseEnum.REQUEST_MORE_ERROR);
        }
        // 查出传进来的所有商品的商品id 放在set中
        Set<Integer> productIdSets = orderProduct.stream().map(Cart::getProductId).collect(Collectors.toSet());
        // 通过productIdSet 查出商品 list
        List<Product> productList = productExtMapper.selectByProductIdSet(productIdSets);
        // 通过商品 id 将list 变成map
        Map<Integer, Product> map = productList.stream().collect(Collectors.toMap(Product::getId, product -> product));
        //唯一订单id
        Long orderNo = generateOrderNo();
        //订单详情表 一对多， order 对 多orderItem
        List<OrderItem> orderItemList =new ArrayList<>();
        for (Cart cart : orderProduct) {
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
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR,"商品："+product.getName()+"：库存不正确 ");
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
        //生成订单，入库 Order ,事务
        Order order = buildOrder(uid, orderNo, orderCreateRequest, orderItemList);
        int rowForOrder = orderMapper.insertSelective(order);
        if(rowForOrder <= 0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        int rowForOrderItem = orderItemExtMapper.batchInsert(orderItemList);
        if(rowForOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        // 判断是购物车下单方式还是立即购买方式
        if(orderCreateRequest.getType().equals(CreateOrderTypeEnum.CART.getType())){
            //购物车redisKey
            String cartKey=String.format(MallConsts.CART_REDIS_KEY_TEMPLATE,uid);
            //更新购物车（删除选中的商品） Redis 没有事务 不能回滚 所以确保前面没有发生异常
            for (Cart cart : orderProduct) {
                //先判断redis 里面是否有该购物车数据
                Object value = redisUtil.hGet(cartKey,String.valueOf(cart.getProductId()));
                if(value != null) {
                    redisUtil.hDelete(cartKey, String.valueOf(cart.getProductId()));
                }
            }
        }

        //设置订单ip访问
        redisUtil.setEx(orderKey,"ADD_ORDER",60,TimeUnit.SECONDS);

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
        if(user == null){
            return ResponseVo.error(ResponseEnum.USER_NOT_EXIST);
        }
        PageHelper.startPage(pageNum,pageSize);
        OrderExample example = new OrderExample();
        example.createCriteria().andUserIdEqualTo(uid);
        example.setOrderByClause("`create_time` DESC");
        List<Order> orderList = orderMapper.selectByExample(example);
        Set<Long> orderNoSet = orderList.stream().map(Order::getOrderNo).collect(Collectors.toSet());

        List<OrderItem> orderItemList = orderItemExtMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItem::getOrderNo));
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

    @Override
    public ResponseVo delete(Integer uid, Long orderNo) {
        User user = userMapper.selectByPrimaryKey(uid);
        if(user == null){
            return ResponseVo.error(ResponseEnum.USER_NOT_EXIST);
        }
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<Order> orderList = orderMapper.selectByExample(example);
        Order order = orderList.get(0);
        if(user.getRole().equals(RoleEnum.ADMIN.getCode())){
            if(CollectionUtils.isEmpty(orderList)){
                return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
            }
        }else{
            if (CollectionUtils.isEmpty(orderList) || !order.getUserId().equals(uid)) {
                return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
            }
        }
        if(!(order.getStatus().equals(OrderStatusEnum.CANCELED.getCode()) ||
            order.getStatus().equals(OrderStatusEnum.TRADE_CLOSE.getCode()) ||
                order.getStatus().equals(OrderStatusEnum.TRADE_SUCCESS.getCode()))
        ){
            return ResponseVo.error(ResponseEnum.ORDER_DELETE_STATUS_ERROR);
        }
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(orderNo);
        int row = orderMapper.deleteByExample(orderExample);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.ORDER_DELETE_ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 支付后修改订单状态
     *
     * @param orderNo
     */
    @Override
    public void paid(Long orderNo,Integer payPlatform) {
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
        order.setPaymentType(payPlatform);
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
            order.setCloseTime(new Date());
            int row = orderMapper.updateByPrimaryKeySelective(order);
            if(row <= 0){
                throw new RuntimeException("将超时未支付订单自动取消失败，订单id:" + orderNo);
            }
        }
    }
}
