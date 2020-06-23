package com.wals.pipe.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-07 21:22:00
 */
@Data
@TableName("tb_banner_img")
public class Banner {

    @TableId
    private int id;

    @TableField
    private String imgUrl;

    @TableField
    private int productId;

    @TableField
    private int bannerIndex;

}