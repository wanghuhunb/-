package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.Orders;
import com.itheima.entity.R;
import com.itheima.entity.dto.PageDto;


public interface OrderService extends IService<Orders> {
    R submit(Orders order);

    R findPage(PageDto pageDtoe);
}