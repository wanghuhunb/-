package com.itheima.entity.dto;

import lombok.Data;

import javax.xml.ws.Service;

@Data
public class UserDto {
    private String phone;
    /**
     * 修改默认地址的时候的id
     */
    private Long id;
}
