package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.R;
import com.itheima.entity.User;
import com.itheima.entity.dto.LoginDto;
import com.itheima.entity.dto.LoginPhoneDto;
import com.itheima.mapper.UserMapper;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 登录功能
     * @param
     * @return
     */
    @Override
    public R login(LoginPhoneDto loginPhoneDto) {
        //判断数据库有没有这个用户
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,loginPhoneDto.getPhone());
        User user = userMapper.selectOne(wrapper);
        if (user==null){
            //没有这个用户给他注册一个
            User user1 = new User();
            user1.setPhone(loginPhoneDto.getPhone());
            user1.setStatus(1);
            userMapper.insert(user1);
            return R.success(user1);
        }
        //有这个用户登陆成功
        return R.success(user);
    }


}
