package com.wals.pipe.controller;

import com.wals.pipe.entity.Banner;
import com.wals.pipe.entity.Product;
import com.wals.pipe.entity.ProductBanner;
import com.wals.pipe.service.BannerService;
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
 * @createTime 2020-04-07 21:14:00
 */
@RestController
@RequestMapping("/banner")
public class BannerController {


    @Autowired
    private BannerService bannerService;

    @Autowired
    private ProductService productService;

    @GetMapping("/getBanner")
    public Result getBanner(){
        return bannerService.getBanner();
    }



    @GetMapping("/carousels")
    public ModelAndView carouselPage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("admin/newbee_mall_carousel");

        request.setAttribute("path", "newbee_mall_carousel");

        Result result = productService.getProductList();
        request.setAttribute("productList",result.getData());

        return mv;
    }


    @PostMapping("/insertProductBanner")
    public Result insertProductBanner(@RequestBody Banner banner){
        return bannerService.insertProductBanner(banner);
    }

    @GetMapping("/deleteBanner")
    public Result deleteBanner(@RequestParam("ids") String ids){
        bannerService.deleteBanner(ids);
        return Result.getInstance(true,true);
    }


    @RequestMapping(value = "/banner/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {

        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(bannerService.bannerListPage(pageUtil));
    }
}