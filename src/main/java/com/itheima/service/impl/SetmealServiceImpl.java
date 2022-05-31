package com.itheima.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.R;
import com.itheima.entity.Setmeal;
import com.itheima.entity.SetmealDish;
import com.itheima.entity.dto.PageDto;
import com.itheima.entity.dto.SetmealDto;
import com.itheima.mapper.CategoryMapper;
import com.itheima.mapper.SetmealDishMapper;
import com.itheima.mapper.SetmealMapper;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * 套餐实现类
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加套餐
     *
     * @param
     * @return
     */
    @Override
    public R addSetmeal(SetmealDto setmealDto) {
        //校验参数
        if (setmealDto == null) {
            return R.error("参数异常");
        }
        //添加套餐表
        setmealMapper.insert(setmealDto);
        //添加套餐,菜品中间表
        //补全参数
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            //setmealDishMapper.insert(setmealDish);
        }
        setmealDishService.saveBatch(setmealDishes);
        return R.success("添加成功");
    }

    @Override
    public R fandPage(PageDto pageDto) {
        //校验参数
        if (pageDto == null) {
            return R.error("参数异常");
        }
        Page<Setmeal> page = new Page<>(pageDto.getPage(),pageDto.getPageSize());
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(pageDto.getName())){
            wrapper.like(Setmeal::getName,pageDto.getName());
        }
        Page<Setmeal> page1 = setmealMapper.selectPage(page, wrapper);
        //进行套餐分类的添加
        Page<SetmealDto> page2 = new Page<>();

        BeanUtils.copyProperties(page1,page2);

        List<Setmeal> records1 = page1.getRecords();

        String s = JSON.toJSONString(records1);

        List<SetmealDto> records = JSON.parseArray(s, SetmealDto.class);

        for (SetmealDto record : records) {
            String name = categoryMapper.selectById(record.getCategoryId()).getName();
            record.setCategoryName(name);
        }
        page2.setRecords(records);
        return R.success(page2);
    }

    @Override
    public R deleteByids(List<Long> ids) {
        if(ids.size()==0){
            return R.error("参数异常");
        }
        //判断套餐是否在销售中
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getStatus,"1");//表示正处在销售
        wrapper.in(Setmeal::getId,ids);
        Integer integer = setmealMapper.selectCount(wrapper);
        if (integer>0){
            return R.error("销售中,无法删除");
        }
        //可以删除 先删除中间表
        LambdaUpdateWrapper<SetmealDish> wrapperde=new LambdaUpdateWrapper<>();
        wrapperde.in(SetmealDish::getSetmealId,ids);
        setmealDishMapper.delete(wrapperde);
        //再删除主表
        setmealMapper.deleteBatchIds(ids);
        return R.success("删除成功");
    }

    /**
     * 修改功能数据回显
     * @param id
     * @return
     */
    @Override
    public R fingByOne(Long id) {
        if(id==null){
            return R.error("参数异常");
        }
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(wrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return R.success(setmealDto);
    }

    /**
     * 套餐修改功能
     * @param setmealDto
     * @return
     */
    @Override
    public R modify(SetmealDto setmealDto) {
        if(setmealDto==null){
            return R.error("参数异常");
        }
        //先删除套餐里面的所有菜品
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishMapper.delete(wrapper);
        //在添加套餐里面的所有菜品
        //添加字段
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
        //修改套餐的主表
        setmealMapper.updateById(setmealDto);
        return R.success("修改成功");
    }

    /**
     * 批量停售和起售
     * @param id
     * @param ids
     * @return
     */
    @Override
    public R startAndEnd(Integer id, Long[] ids) {
        if(id==null&&ids.length==0){
            return R.error("参数异常");
        }
        LambdaUpdateWrapper<Setmeal> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(id);
        setmealMapper.update(setmeal,wrapper);
        return R.success("修改成功");
    }

    /**
     * 查询套餐
     * @param setmeal
     * @return
     */
    @Override
    public R fandList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> setmeals = setmealMapper.selectList(wrapper);
        return R.success(setmeals);
    }
}
