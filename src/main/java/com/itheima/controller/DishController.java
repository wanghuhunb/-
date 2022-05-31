package com.itheima.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.itheima.entity.R;
import com.itheima.entity.dto.DishDto;
import com.itheima.service.DishService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品接口
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R add(@RequestBody DishDto dishDto) {
        R r = dishService.addAll(dishDto);
        return r;
    }

    /**
     * 分夜查找
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R page(Integer page, Integer pageSize, String name) {
        return dishService.pageInfo(page, pageSize, name);
    }

    /**
     * 修改菜品数据回显
     *
     * @return
     */
    @GetMapping("/{id}")
    public R findById(@PathVariable("id") Long id) {
        return dishService.findById(id);
    }

    /**
     * 修改菜品
     *
     * @return
     */
    @PutMapping()
    public R modify(@RequestBody DishDto dishDto) {
        return dishService.modify(dishDto);
    }

    /**
     * 批量删除
     */
    @DeleteMapping()
    public R delete(Long[] ids) {
      return   dishService.deleteByIds(ids);
    }
    /**
     * 批量起售
     */
    @PostMapping("/status/1")
    public R start(Long[] ids){
      return   dishService.start(ids);
    }
    /**
     * 批量停售
     */
    @PostMapping("/status/0")
    public R updown(Long[] ids){
        return   dishService.updown(ids);
    }
    /**
     * 更具分类id查询菜品信息
     */
    @GetMapping("/list")
    public R list(String categoryId){
       return dishService.findListBycategoryId(categoryId);
    }
}
