package com.wals.pipe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wals.pipe.entity.Collection;
import com.wals.pipe.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-21 21:17:00
 */
public interface CollectMapper extends BaseMapper<Collection> {


    List<Product> getUserProduct(int userId);

}
