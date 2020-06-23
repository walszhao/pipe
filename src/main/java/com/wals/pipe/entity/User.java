package com.wals.pipe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-06 09:10:00
 */
@Data
@TableName("tb_user")
public class User {

    @TableId(type= IdType.AUTO)
    private int id;

    @TableField
    private String openid;

    @TableField
    private String sessionKey;

    @TableField
    private String phoneNumber;

    @TableField
    private Date createTime;

    @TableField
    private Date updateTime;

    @TableField
    private String loginStatus;
}