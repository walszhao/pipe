package com.wals.pipe.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.wals.pipe.entity.Collection;
import com.wals.pipe.entity.Product;
import com.wals.pipe.mapper.CollectMapper;
import com.wals.pipe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-21 21:16:00
 */
@Service("collectService")
public class CollectService {

    @Autowired
    private CollectMapper collectMapper;

    public Result collectProduct(Collection collection){
        

        collectMapper.insert(collection);

        return Result.getInstance("收藏成功", true);

    }


    public Result hasCollected(int userId, int productId){

        Result result = Result.getInstance();
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("product_id",productId);

        Collection collection = collectMapper.selectOne(queryWrapper);
        if (collection != null){
            result.setSuccess(true);
            result.setData(true);
        }else{
            result.setSuccess(true);
            result.setData(false);
        }
        return result;
    }

    public Result cancelCollect(int userId, int productId){

        Map<String,Object> coumnMap = new HashMap<>();
        coumnMap.put("user_id",userId);
        coumnMap.put("product_id",productId);
        int rows = collectMapper.deleteByMap(coumnMap);
        System.out.println("影响记录数："+rows);

        return Result.getInstance(true,true);
    }


    public Result getUserCollect(int userId){
        List<Product> result = collectMapper.getUserProduct(userId);
        return Result.getInstance(true,result);
    }
}