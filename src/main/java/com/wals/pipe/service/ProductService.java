package com.wals.pipe.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wals.pipe.entity.Product;
import com.wals.pipe.entity.ProductBanner;
import com.wals.pipe.mapper.ProductBannerMapper;
import com.wals.pipe.mapper.ProductMapper;
import com.wals.pipe.utils.PageQueryUtil;
import com.wals.pipe.utils.PageResult;
import com.wals.pipe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-19 13:07:00
 */
@Service("productService")
public class ProductService {


    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductBannerMapper productBannerMapper;


    public Result getProductList() {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 2);

        List<Product> productList = productMapper.selectList(queryWrapper);

        return Result.getInstance(true, productList);
    }


    public Result getProductById(int id) {

        Product product = productMapper.selectById(id);

        QueryWrapper<ProductBanner> queryImgWrapper = new QueryWrapper<>();
        queryImgWrapper.eq("product_id", id).orderByAsc("sort");

        List<ProductBanner> productList = productBannerMapper.selectList(queryImgWrapper);
        product.setProductBannerList(productList);

        return Result.getInstance(true, product);
    }


    public Result searchProduct(String keyWords) {

        QueryWrapper<Product> queryImgWrapper = new QueryWrapper<>();
        queryImgWrapper.like("product_name", keyWords);

        List<Product> productList = productMapper.selectList(queryImgWrapper);

        return Result.getInstance(true, productList);

    }


    public PageResult getProductPage(PageQueryUtil pageUtil) {

        IPage<Product> page = new Page<>(pageUtil.getPage(),pageUtil.getLimit());


        page = productMapper.selectPage(page, new QueryWrapper<Product>());

        PageResult pageResult = new PageResult(page.getRecords(),Integer.parseInt(String.valueOf(page.getTotal())),
                Integer.parseInt(String.valueOf(page.getSize())), Integer.parseInt(String.valueOf(page.getCurrent())));


        return pageResult;
    }

    public Result saveProduct(Product product){

        productMapper.insert(product);

        List<String> bannerList = product.getBannerList();
        if (bannerList != null &&  bannerList.size() > 0){
            for (String s : bannerList) {
                ProductBanner productBanner = new ProductBanner();
                productBanner.setImgUrl(s);
                productBanner.setProductId(product.getId());
                productBannerMapper.insert(productBanner);
            }
        }
        //productMapper.insert(product);
        return new Result(true);
    }


    public Result updateProduct(Product product){

        int productId = product.getId();

        QueryWrapper<ProductBanner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        productBannerMapper.delete(queryWrapper);

        List<String> bannerList = product.getBannerList();
        if (bannerList != null &&  bannerList.size() > 0){
            for (String s : bannerList) {
                ProductBanner productBanner = new ProductBanner();
                productBanner.setImgUrl(s);
                productBanner.setProductId(product.getId());
                productBannerMapper.insert(productBanner);
            }
        }

        productMapper.updateById(product);

        return Result.getInstance("更新成功", true);

    }



    public Result deleteProduct(String ids){

        List<Integer> idList = new ArrayList<>();

        JSONArray idJsonArr = JSONObject.parseArray(ids);
        if (idJsonArr !=null &&  idJsonArr.size() >0){
            for (Object o : idJsonArr) {
                String idStr = o.toString();
                idList.add(Integer.parseInt(idStr));
            }
        }


        productMapper.deleteBatchIds(idList);
        return Result.getInstance(true,true);

    }


    public void updateStatus(String ids, int status){

        List<Integer> idList = new ArrayList<>();

        JSONArray idJsonArr = JSONObject.parseArray(ids);
        if (idJsonArr !=null &&  idJsonArr.size() >0){
            for (Object o : idJsonArr) {
                String idStr = o.toString();
                int id = Integer.parseInt(idStr);

                Product product = new Product();
                product.setId(id);
                product.setStatus(status);
                productMapper.updateById(product);
            }
        }

    }
}