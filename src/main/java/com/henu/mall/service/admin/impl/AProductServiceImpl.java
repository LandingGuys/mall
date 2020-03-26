package com.henu.mall.service.admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.mall.enums.ResponseEnum;
import com.henu.mall.mapper.ProductExtMapper;
import com.henu.mall.mapper.ProductMapper;
import com.henu.mall.pojo.Product;
import com.henu.mall.request.ProductAddRequest;
import com.henu.mall.request.ProductUpdateRequest;
import com.henu.mall.service.admin.AProductService;
import com.henu.mall.service.member.CategoryService;
import com.henu.mall.vo.ProductDetailVo;
import com.henu.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.henu.mall.enums.ResponseEnum.PRODUCT_NOT_EXIST;
import static com.henu.mall.enums.SaleEnum.DELETE;

/**
 * @author lv
 * @date 2020-03-26 10:43
 */
@Service
public class AProductServiceImpl implements AProductService {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductExtMapper productExtMapper;

    @Resource
    private ProductMapper productMapper;


    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize,String query) {
        Set<Integer> categoryIdSet =new HashSet<>();
        //查找类目的所有类目（包括子类目）
        if(categoryId !=null){
            categoryService.findSubCategoryId(categoryId,categoryIdSet);
            categoryIdSet.add(categoryId);
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productExtMapper.selectAllByCondition(categoryIdSet,query);
        List<ProductDetailVo> productDetailVoList = products.stream().map(e -> {
            ProductDetailVo productDetailVo = new ProductDetailVo();
            BeanUtils.copyProperties(e, productDetailVo);
            return productDetailVo;
        }).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo<>(products);
        pageInfo.setList(productDetailVoList);

        return ResponseVo.success(pageInfo);
    }

    /**
     * 新增商品
     * @param request
     * @return
     */
    @Override
    public ResponseVo add(ProductAddRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request,product);
        int row = productMapper.insertSelective(product);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_ADD_ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 更新商品
     *
     * @param request
     * @return
     */
    @Override
    public ResponseVo<ProductDetailVo> update(ProductUpdateRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request,product);
        Product selectByPrimaryKey = productMapper.selectByPrimaryKey(product.getId());
        if(selectByPrimaryKey == null){
            ResponseVo.error(PRODUCT_NOT_EXIST);
        }
        int row = productMapper.updateByPrimaryKeySelective(product);
        if(row <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_UPDATE_ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 删除商品
     *
     * @param productId
     * @return
     */
    @Override
    public ResponseVo delete(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ResponseVo.error(PRODUCT_NOT_EXIST);
        }
        Product deleteProduct = new Product();
        deleteProduct.setId(productId);
        deleteProduct.setStatus(DELETE.getStatus());
        int row = productMapper.updateByPrimaryKeySelective(deleteProduct);
        if( row <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_DELETE_ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null || product.getStatus().equals(DELETE.getStatus())){
            ResponseVo.error(PRODUCT_NOT_EXIST);
        }
        ProductDetailVo productDetailVo = new ProductDetailVo();

        BeanUtils.copyProperties(product,productDetailVo);
        //敏感数据处理
        productDetailVo.setStock(product.getStock()>9999 ? 9999 : product.getStock());

        return ResponseVo.success(productDetailVo);
    }


}
