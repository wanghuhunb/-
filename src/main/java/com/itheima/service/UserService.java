package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.R;
import com.itheima.entity.User;
import com.itheima.entity.dto.LoginDto;
import com.itheima.entity.dto.LoginPhoneDto;

/**
 * 用户接口
 */
public interface UserService extends IService<User> {

    R login(LoginPhoneDto loginPhoneDto);
}
