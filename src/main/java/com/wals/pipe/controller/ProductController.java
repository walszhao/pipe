package com.wals.pipe.controller;

import com.wals.pipe.entity.Product;
import com.wals.pipe.service.ClassifyService;
import com.wals.pipe.service.ProductService;
import com.wals.pipe.utils.PageQueryUtil;
import com.wals.pipe.utils.Result;
import com.wals.pipe.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-19 13:05:00
 */

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ClassifyService classifyService;

    @GetMapping("/getProductList")
    @ResponseBody
    public Result getProductList() {
        return productService.getProductList();
    }


    @GetMapping("/getProductById")
    @ResponseBody
    public Result getProductById(@RequestParam("id") int id) {
        return productService.getProductById(id);
    }


    @GetMapping("/searchProduct")
    @ResponseBody
    public Result searchProduct(@RequestParam("keyWords") String keyWords) {
        return productService.searchProduct(keyWords);
    }


    /**
     * 列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {

        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(productService.getProductPage(pageUtil));
    }


    @GetMapping("/goods/edit")
    public ModelAndView edit(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("admin/newbee_mall_goods_edit");
        request.setAttribute("path", "edit");
        //查询所有的一级分类
        Result firstLevelCategories = classifyService.getClassifyList();
        request.setAttribute("firstLevelCategories", firstLevelCategories.getData());
        return modelAndView;
    }

    @PostMapping("/saveProduct")
    public Result saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PostMapping("/updateProduct")
    public Result updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }


    @GetMapping("/goods/edit/{goodsId}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        ModelAndView modelAndView = new ModelAndView("admin/newbee_mall_goods_edit");
        request.setAttribute("path", "edit");
        Result result = productService.getProductById(Integer.parseInt(goodsId.toString()));
        Product product = (Product) result.getData();
        if (product == null) {
            modelAndView.setViewName("/login");
        }else{
            request.setAttribute("product", product);
            request.setAttribute("path", "goods-edit");
            //查询所有的一级分类
            Result firstLevelCategories = classifyService.getClassifyList();
            request.setAttribute("firstLevelCategories", firstLevelCategories.getData());
        }

        return modelAndView;
    }

    @GetMapping("/deleteProduct")
    public Result deleteProduct(@RequestParam("ids") String ids){
        productService.deleteProduct(ids);
        return Result.getInstance(true,true);
    }

    @GetMapping("/online")
    public Result online(@RequestParam("ids") String ids){
        productService.updateStatus(ids,2);
        return Result.getInstance(true,true);
    }


    @GetMapping("/offline")
    public Result offline(@RequestParam("ids") String ids){
        productService.updateStatus(ids,1);
        return Result.getInstance(true,true);
    }
}