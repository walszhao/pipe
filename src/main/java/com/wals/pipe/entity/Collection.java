package com.wals.pipe.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-21 21:23:00
 */
@Data
@TableName("tb_user_product")
public class Collection {


    @TableId
    private int id;

    @TableField
    private int userId;

    @TableField
    private int productId;
}