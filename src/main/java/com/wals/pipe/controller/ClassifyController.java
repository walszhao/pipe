package com.wals.pipe.controller;

import com.wals.pipe.entity.Classify;
import com.wals.pipe.entity.Product;
import com.wals.pipe.service.ClassifyService;
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
 * @createTime 2020-04-28 20:27:00
 */

@RestController
@RequestMapping("/classify")
public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;


    @GetMapping("/getClassifyList")
    @ResponseBody
    public Result getClassifyList(){
        return classifyService.getClassifyList();
    }

    @GetMapping("/getProductListByClassify")
    @ResponseBody
    public Result getProductListByClassify(@RequestParam("classifyId") int classifyId){
        return classifyService.getProductListByClassify(classifyId);
    }

    @GetMapping("/categories")
    public ModelAndView  categoriesPage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("admin/newbee_mall_category");
        request.setAttribute("path", "newbee_mall_category");
        return mv;
    }


    @PostMapping("/insertCategories")
    public Result insertCategories(@RequestBody Classify classify){
        return classifyService.insertClassify(classify);
    }

    @PostMapping("/updateClassify")
    public Result updateClassify(@RequestBody Classify classify){
        return  classifyService.updateClassify(classify);
    }

    @GetMapping("/getClassifyListPage")
    public Result classifyList(@RequestParam Map<String, Object> params){
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(classifyService.getCategorisPage(pageUtil));
    }


    @GetMapping("/deleteClassify")
    public Result deleteClassify(@RequestParam("ids") String ids){
        return classifyService.deleteClassify(ids);
    }
}