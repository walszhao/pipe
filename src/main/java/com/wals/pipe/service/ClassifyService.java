package com.wals.pipe.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wals.pipe.entity.Classify;
import com.wals.pipe.entity.Product;
import com.wals.pipe.mapper.ClassifyMapper;
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
 * @createTime 2020-04-28 20:29:00
 */

@Service("classifyService")
public class ClassifyService {



    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private ProductMapper productMapper;

    public Result getClassifyList(){

        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("classify_index");

        List<Classify> classifyList = classifyMapper.selectList(queryWrapper);

        return Result.getInstance(true,classifyList);
    }


    public Result getProductListByClassify( int classifyId){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classify_id",classifyId);

        List<Product> productList = productMapper.selectList(queryWrapper);

        return Result.getInstance(true,productList);
    }


    public Result insertClassify(Classify classify){
        classifyMapper.insert(classify);
        return Result.getInstance(true,true);
    }


    public Result updateClassify(Classify classify){
        classifyMapper.updateById(classify);
        return Result.getInstance(true,true);
    }


    public PageResult getCategorisPage(PageQueryUtil pageUtil){
        IPage<Classify> page = new Page<>(pageUtil.getPage(),pageUtil.getLimit());

        page = classifyMapper.selectPage(page, new QueryWrapper<Classify>());

        PageResult pageResult = new PageResult(page.getRecords(),Integer.parseInt(String.valueOf(page.getTotal())),
                Integer.parseInt(String.valueOf(page.getSize())), Integer.parseInt(String.valueOf(page.getCurrent())));


        return pageResult;
    }


    public Result deleteClassify(String ids){

        List<Integer> idList = new ArrayList<>();

        JSONArray idJsonArr = JSONObject.parseArray(ids);
        if (idJsonArr !=null &&  idJsonArr.size() >0){
            for (Object o : idJsonArr) {
                String idStr = o.toString();
                idList.add(Integer.parseInt(idStr));
            }
        }


        classifyMapper.deleteBatchIds(idList);
        return Result.getInstance(true,true);
    }

}