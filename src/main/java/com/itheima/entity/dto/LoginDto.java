package com.itheima.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录接受类
 */
@Data
public class LoginDto  implements Serializable {
    private String username;
    private String password;
}
