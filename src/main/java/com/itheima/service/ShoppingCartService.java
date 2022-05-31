package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.R;
import com.itheima.entity.ShoppingCart;
import com.itheima.entity.dto.DishDto;

public interface ShoppingCartService extends IService<ShoppingCart> {
    R findAll();

    R add(ShoppingCart shoppingCart);

    R cleanById(long id);

    R sub(DishDto dishDto);
}
