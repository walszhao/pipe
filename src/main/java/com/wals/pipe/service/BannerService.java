package com.wals.pipe.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wals.pipe.entity.Banner;
import com.wals.pipe.entity.Product;
import com.wals.pipe.entity.ProductBanner;
import com.wals.pipe.mapper.BannerMapper;
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
 * @createTime 2020-04-07 21:30:00
 */
@Service("bannerService")
public class BannerService {

    @Autowired
    private BannerMapper bannerMapper;


    public Result getBanner(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("banner_index");
        List<Banner> bannerList = bannerMapper.selectList(queryWrapper);

        return Result.getInstance(true,bannerList);
    }

    public Result insertProductBanner(Banner banner) {

        bannerMapper.insert(banner);

        return Result.getInstance(true,true);
    }


    public Result deleteBanner( String ids){
        List<Integer> idList = new ArrayList<>();

        JSONArray idJsonArr = JSONObject.parseArray(ids);
        if (idJsonArr !=null &&  idJsonArr.size() >0){
            for (Object o : idJsonArr) {
                String idStr = o.toString();
                idList.add(Integer.parseInt(idStr));
            }
        }


        bannerMapper.deleteBatchIds(idList);
        return Result.getInstance(true,true);
    }


    public PageResult bannerListPage(PageQueryUtil pageUtil){
        IPage<Banner> page = new Page<>(pageUtil.getPage(),pageUtil.getLimit());


        page = bannerMapper.selectPage(page, new QueryWrapper<Banner>());

        PageResult pageResult = new PageResult(page.getRecords(),Integer.parseInt(String.valueOf(page.getTotal())),
                Integer.parseInt(String.valueOf(page.getSize())), Integer.parseInt(String.valueOf(page.getCurrent())));


        return pageResult;
    }
}