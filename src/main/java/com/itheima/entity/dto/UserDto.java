package com.itheima.entity.dto;

import lombok.Data;

import javax.xml.ws.Service;
import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private String phone;
    /**
     * 修改默认地址的时候的id
     */
    private Long id;
}
