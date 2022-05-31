package com.itheima.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.SetmealDish;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SetmealDishService extends IService<SetmealDish> {
}
