package com.itheima.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginPhoneDto implements Serializable {
    private String code;
    private String phone;
}
