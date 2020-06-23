package com.wals.pipe.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-20 21:18:00
 */
@Data
@TableName("tb_product_banner")
public class ProductBanner {

    @TableId
    private int id;

    @TableField
    private int productId;

    @TableField
    private String imgUrl;

    @TableField
    private int sort;
}