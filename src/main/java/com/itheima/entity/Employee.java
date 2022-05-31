package com.itheima.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 员工类
 */
@Data
@TableName("employee")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    @TableField("id_number")
    private String idNumber; //驼峰命名法 ---> 映射的字段名为 id_number

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}