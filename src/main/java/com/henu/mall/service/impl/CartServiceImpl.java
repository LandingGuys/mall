package com.henu.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.henu.mall.consts.MallConsts;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.enums.SaleEnum;
import com.henu.mall.mapper.ProductExtMapper;
import com.henu.mall.mapper.ProductMapper;
import com.henu.mall.pojo.Cart;
import com.henu.mall.pojo.Product;
import com.henu.mall.request.CartAddRequest;
import com.henu.mall.request.CartUpdateRequest;
import com.henu.mall.service.CartService;
import com.henu.mall.utils.RedisUtil;
import com.henu.mall.vo.CartProductVo;
import com.henu.mall.vo.CartVo;
import com.henu.mall.vo.ResponseVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lv
 * @date 2020-02-03 9:42
 */
@Service
public class CartServiceImpl implements CartService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductExtMapper productExtMapper;

    @Resource
    private RedisUtil redisUtil;
    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        //获取redis里购物车列表
        String cartKey=String.format(MallConsts.CART_REDIS_KEY_TEMPLATE,uid);
        Map<Object, Object> cartMaps = redisUtil.hGetAll(cartKey);
        //设置初始参数
        Boolean  selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();

        //获取所有的购物车里商品的id存在set集合中
        Set<Integer> productIdSet = cartMaps.keySet().stream()
                                    .map(e -> Integer.valueOf(e.toString()))
                                    .collect(Collectors.toSet());
        //根据productIdSet,查出所有的商品信息
        List<Product> productList = productExtMapper.selectByProductIdSet(productIdSet);
        //遍历每一个购物车中每一个商品
        for(Map.Entry<Object,Object> entry : cartMaps.entrySet()){
            //获取redis中购物车对象
            Integer productId = Integer.valueOf(entry.getKey().toString());
            Cart cart = JSON.parseObject(entry.getValue().toString(),Cart.class);
            //遍历购物车中所有商品信息，并设置到CartProductVo对象中
            if(CollectionUtils.isNotEmpty(productList)){
                for (Product product : productList) {
                    if(productId.equals(product.getId())){
                        CartProductVo cartProductVo = new CartProductVo(productId,
                                cart.getQuantity(),
                                product.getName(),
                                product.getSubtitle(),
                                product.getMainImage(),
                                product.getPrice(),
                                product.getStatus(),
                                product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                                product.getStock(),
                                cart.getProductSelected());
                        //列表
                        cartProductVoList.add(cartProductVo);
                        //如果有一个没选中就不叫全选
                        if (!cart.getProductSelected()) {
                            selectAll = false;
                        }
                        //计算总价(只计算选中的)
                        if (cart.getProductSelected()) {
                            cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                        }
                    }
                }
            }
            //购物车总数量
            cartTotalQuantity += cart.getQuantity();
        }
        //有一个没有选中，就不叫全选
        cartVo.setSelectAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddRequest cartAddRequest) {
        Integer quantity=cartAddRequest.getProductNum();
        //商品是否存在
        Product product = productMapper.selectByPrimaryKey(cartAddRequest.getProductId());
        if(product == null){
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        //商品是否是在售状态
        if(!product.getStatus().equals(SaleEnum.ON_SALE.getStatus())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }
        //商品库存是否够
        if(product.getStock() <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        Cart cart;
        //购物车redisKey
        String cartKey=String.format(MallConsts.CART_REDIS_KEY_TEMPLATE,uid);
        //先判断redis 里面是否有该购物车数据
        Object value = redisUtil.hGet(cartKey,String.valueOf(product.getId()));
        if(value == null){
            //没有，新增购物车对象
            cart = new Cart(product.getId(),quantity,cartAddRequest.getSelected());
        }else{
            //有，获取购物车对象，再增加数量
            cart = JSON.parseObject(value.toString(), Cart.class);
            cart.setQuantity(cart.getQuantity()+quantity);
        }
        //put 到redis 里
        redisUtil.hPut(cartKey,String.valueOf(product.getId()), JSON.toJSONString(cart));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid,CartUpdateRequest cartUpdateRequest) {
        //购物车redisKey
        String cartKey=String.format(MallConsts.CART_REDIS_KEY_TEMPLATE,uid);
        //先判断redis 里面是否有该购物车数据
        Object value = redisUtil.hGet(cartKey,String.valueOf(cartUpdateRequest.getProductId()));
        if(value == null){
            //无，报错购物车中无该商品
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        Cart cart = JSON.parseObject(value.toString(),Cart.class);
        if (cartUpdateRequest.getQuantity() != null
                && cartUpdateRequest.getQuantity() >= 0) {
            cart.setQuantity(cartUpdateRequest.getQuantity());
        }
        if (cartUpdateRequest.getSelected() != null) {
            cart.setProductSelected(cartUpdateRequest.getSelected());
        }
        redisUtil.hPut(cartKey,String.valueOf(cartUpdateRequest.getProductId()),JSON.toJSONString(cart));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid,Integer productId) {
        //购物车redisKey
        String cartKey=String.format(MallConsts.CART_REDIS_KEY_TEMPLATE,uid);
        //先判断redis 里面是否有该购物车数据
        Object value = redisUtil.hGet(cartKey,String.valueOf(productId));
        if(value == null){
            //无
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        redisUtil.hDelete(cartKey,String.valueOf(productId));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> isSelectAll(Integer uid,Boolean selectAll) {
        String cartKey=String.format(MallConsts.CART_REDIS_KEY_TEMPLATE,uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(selectAll);
            redisUtil.hPut(cartKey,String.valueOf(cart.getProductId()),JSON.toJSONString(cart));
        }

        return list(uid);
    }

//    @Override
//    public ResponseVo<CartVo> unSelectAll(Integer uid) {
//        String cartKey=String.format(MallConsts.CART_REDIS_KEY_TEMPLATE,uid);
//
//        for (Cart cart : listForCart(uid)) {
//            cart.setProductSelected(false);
//            redisUtil.hPut(cartKey,String.valueOf(cart.getProductId()),JSON.toJSONString(cart));
//        }
//
//        return list(uid);
//
//    }

    @Override
    public ResponseVo<Integer> productSum(Integer uid) {
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);
        return ResponseVo.success(sum);
    }
    @Override
    public List<Cart> listForCart(Integer uid) {
        //获取redis里购物车列表
        String cartKey=String.format(MallConsts.CART_REDIS_KEY_TEMPLATE,uid);
        Map<Object, Object> cartMaps = redisUtil.hGetAll(cartKey);
        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<Object,Object> entry : cartMaps.entrySet()) {
            cartList.add(JSON.parseObject(entry.getValue().toString(),Cart.class));
        }
        return cartList;
    }
}
