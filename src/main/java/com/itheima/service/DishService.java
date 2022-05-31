package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.Dish;
import com.itheima.entity.R;
import com.itheima.entity.dto.DishDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜品
 */
@Transactional
public interface DishService extends IService<Dish> {
    R addAll(DishDto dishDto);

    R pageInfo(Integer page, Integer pageSize,String name);

    R findById(Long id);

    R modify(DishDto dishDto);

    R deleteByIds(Long[] ids);

    R start(Long[] ids);

    R updown(Long[] ids);

    R findListBycategoryId(String categoryId);
}
