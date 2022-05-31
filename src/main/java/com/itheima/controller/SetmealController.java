package com.itheima.controller;

import com.itheima.entity.R;

import com.itheima.entity.Setmeal;
import com.itheima.entity.dto.PageDto;
import com.itheima.entity.dto.SetmealDto;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐的相关请求
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 添加套餐
     *
     * @param
     * @return
     */
    @PostMapping
    public R addSetmeal(@RequestBody SetmealDto setmealDto) {
        return setmealService.addSetmeal(setmealDto);
    }

    /**
     * 套餐分页查找
     */
    @GetMapping("/page")
    public R page(PageDto pageDto) {
        return setmealService.fandPage(pageDto);
    }

    /**
     * 删除套餐
     */
    @DeleteMapping
    public R deletesetmeal(@RequestParam List<Long> ids) {
        return setmealService.deleteByids(ids);
    }

    /**
     * 修改套餐回显
     */
    @GetMapping("/{id}")
    public R selectByOne(@PathVariable("id") Long id) {

        return setmealService.fingByOne(id);
    }

    /**
     * 修改保存功能
     */
    @PutMapping()
    public R mofify(@RequestBody SetmealDto setmealDto) {
        return setmealService.modify(setmealDto);
    }

    //批量起售
    @PostMapping("/status/{id}")
    public R startAndEnd(@PathVariable("id") Integer id, Long[] ids) {
        return setmealService.startAndEnd(id, ids);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("list")
    public R listSetmeal(Setmeal setmeal) {
        return setmealService.fandList(setmeal);
    }
}