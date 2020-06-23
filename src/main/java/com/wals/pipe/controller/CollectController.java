package com.wals.pipe.controller;

import com.wals.pipe.entity.Collection;
import com.wals.pipe.service.CollectService;
import com.wals.pipe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-21 21:14:00
 */
@RestController
@RequestMapping("/collect")
public class CollectController {


    @Autowired
    private CollectService collectService;

    @PostMapping("/collectProduct")
    @ResponseBody
    public Result collectProduct(@RequestBody Collection collection){
        return collectService.collectProduct(collection);
    }



    @GetMapping("/hasCollected")
    @ResponseBody
    public Result hasCollected(@RequestParam("userId")int userId,@RequestParam("productId") int productId){
        return collectService.hasCollected(userId,productId);
    }


    @GetMapping("/cancelCollect")
    @ResponseBody
    public Result cancelCollect(@RequestParam("userId")int userId,@RequestParam("productId") int productId){
        return collectService.cancelCollect(userId,productId);
    }

    @GetMapping("/getUserCollected")
    @ResponseBody
    public Result getUserCollected(@RequestParam("userId")int userId){
        return collectService.getUserCollect(userId);
    }


}