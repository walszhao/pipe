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
@TableName("tb_user_admin")
public class AdminUser {


    @TableId
    private int id;

    @TableField
    private String username;

    @TableField
    private String password;

    @TableField
    private String nickName;

    @TableField(exist = false)
    private String originalPassword;

    @TableField(exist = false)
    private String newPassword;



}