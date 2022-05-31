package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.Orders;
import com.itheima.entity.R;


public interface OrderService extends IService<Orders> {
    R submit(Orders order);
}