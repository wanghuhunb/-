package com.itheima.controller;

import com.itheima.config.BaseContest;
import com.itheima.entity.R;
import com.itheima.entity.ShoppingCart;
import com.itheima.entity.dto.DishDto;
import com.itheima.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 擦哈找购物车
      * @return
     */
    @GetMapping("list")
    public R findAll() {
        return shoppingCartService.findAll();
    }
    /**
     * 添加购物车
     */
    @PostMapping("/add")
    public R add(@RequestBody ShoppingCart shoppingCart){
     return  shoppingCartService.add(shoppingCart);
    }
    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public R clean(){
        long id = BaseContest.getId();
      return   shoppingCartService.cleanById(id);
    }
    /**
     * 修改购物车的物品数量
     */
    @PostMapping("/sub")
    public R sub(@RequestBody DishDto dishDto){
       return shoppingCartService.sub(dishDto);
    }
}
