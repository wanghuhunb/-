package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.DishFlavor;

import java.util.List;

public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    int addAll(List<DishFlavor> flavors);
}
