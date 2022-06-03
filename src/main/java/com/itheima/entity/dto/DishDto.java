package com.itheima.entity.dto;

import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import lombok.Data;
import sun.dc.pr.PRError;

import javax.xml.ws.Service;
import java.io.Serializable;
import java.util.List;

/**
 * 添加菜品的参数
 */
@Data
public class DishDto extends Dish implements Serializable {

    private List<DishFlavor> flavors;
    //分类名字
    private String categoryName;
    //套餐id
    private Long setmealId;
    //菜品id
    private Long dishId;
}
