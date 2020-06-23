package com.wals.pipe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-19 13:07:00
 */
@Data
@TableName("tb_product")
public class Product {

    @TableId(type = IdType.AUTO)
    private int id;

    @TableField
    private String productName;

    @TableField
    private BigDecimal originalPrice;

    @TableField
    private BigDecimal presentPrice;

    @TableField
    private String detail;

    @TableField
    private int status;

    @TableField
    private String logoImgUrl;


    @TableField
    private int classifyId;


    @TableField(exist = false)
    private List<ProductBanner> productBannerList;

    @TableField(exist = false)
    private List<String> bannerList;

}