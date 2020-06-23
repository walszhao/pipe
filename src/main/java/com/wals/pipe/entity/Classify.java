package com.wals.pipe.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-28 20:29:00
 */

@Data
@TableName("tb_classify")
public class Classify {



    @TableId
    private int id;

    @TableField
    private String classifyName;


    @TableField
    private String classifyIndex;


}