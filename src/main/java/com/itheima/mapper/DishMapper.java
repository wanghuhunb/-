package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.Dish;
import com.itheima.entity.dto.DishDto;
import org.apache.ibatis.annotations.Insert;

/**
 * 菜品接口
 */
public interface DishMapper extends BaseMapper<Dish> {
    @Insert("INSERT INTO dish values(#{id},#{name},#{categoryId},#{price},#{code},#{image},#{description},#{status},#{sort},#{createTime}," +
            "#{updateTime},#{createUser},#{updateUser},#{isDeleted})")
    void addAll(DishDto dishDto);
}
