package com.itheima.entity.dto;

import com.itheima.entity.Setmeal;
import com.itheima.entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * 添加套餐
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
