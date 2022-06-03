package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import com.itheima.entity.R;
import com.itheima.entity.dto.DishDto;
import com.itheima.mapper.CategoryMapper;
import com.itheima.mapper.DishFlavorMapper;
import com.itheima.mapper.DishMapper;
import com.itheima.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 菜品
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @CachePut(value = "dishID",key="#dishDto.id")  //添加的时候将这个菜品根据id放入缓存中
    @Override
    public R addAll(DishDto dishDto) {
        //参数校验
        if(dishDto==null){
            return R.error("参数异常");
        }
        //雪花算法
        long id = IdWorker.getId();
        dishDto.setId(id);
        dishDto.setSort(0);
        dishDto.setIsDeleted(0);
        //先添加一个菜品表
        dishMapper.addAll(dishDto);
        //再添加口味表
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        dishFlavorMapper.addAll(flavors);

        return R.success("添加成功");
    }

    /**
     * 分页查找
     * @param page
     * @param pageSize
     * @return
     */
    @Cacheable(value = "dishAll",key = "")
    @Override
    public R pageInfo(Integer page, Integer pageSize,String name) {
        Page<Dish> dishPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if(name!=null){
            wrapper.like(Dish::getName,name);
        }
        wrapper.orderByDesc(Dish::getUpdateTime);
        Page<Dish> dishPage1 = dishMapper.selectPage(dishPage, wrapper);
        List<Dish> records = dishPage1.getRecords();

        ArrayList<DishDto> dishDtos = new ArrayList<>();
        for (Dish dish : records) {
            Long categoryId = dish.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            DishDto dishDto = new DishDto();
            dishDto.setCategoryName(category.getName());
            BeanUtils.copyProperties(dish,dishDto);
            dishDtos.add(dishDto);
        }
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage1,dishDtoPage);
        dishDtoPage.setRecords(dishDtos);
        return R.success(dishDtoPage);
    }

    /**
     * 查询回显
     * @param id
     * @return
     */
    @Cacheable(value = "dishID",key = "#id") //查询
    @Override
    public R findById(Long id) {
        Dish dish = dishMapper.selectById(id);
        Long id1 = dish.getId();
        LambdaQueryWrapper<DishFlavor> wirpper =new LambdaQueryWrapper<>();
        wirpper.eq(DishFlavor::getDishId,id1);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(wirpper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(dishFlavors);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @CacheEvict(value = "dishID",key = "#dishDto.id")  //删除
    @Override
    public R modify(DishDto dishDto) {
        //判断参数
        if(dishDto==null){
            return R.error("参数异常");
        }
        //修改菜品
        int i = dishMapper.updateById(dishDto);
        //先删除所有的口味
        LambdaUpdateWrapper<DishFlavor> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorMapper.delete(wrapper);
        //然后添加口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorMapper.addAll(flavors);
        //修改完成后删除所有关于菜品的缓存
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("修改成功");
    }

    @Override
    public R deleteByIds(Long[] ids) {
        //验证参数
        if(ids==null){
            R.error("参数异常");
        }
        //根据id删除掉所有的口味;
        LambdaUpdateWrapper<DishFlavor> wrapper=new LambdaUpdateWrapper<>();
        wrapper.in(DishFlavor::getDishId,ids);
        dishFlavorMapper.delete(wrapper);
        //删掉所有的菜品
        LambdaUpdateWrapper<Dish> wrapper2=new LambdaUpdateWrapper<>();
        wrapper2.in(Dish::getId,ids);
        dishMapper.delete(wrapper2);
        //删除这个菜品对应的分类的缓存 先查出来菜品对应的分类 id都
        //todo


        return R.success("删除成功");
    }

    @Override
    public R start(Long[] ids) {
        //验证参数
        if(ids==null){
            R.error("参数异常");
        }
        //更具菜品id修改状态
        LambdaUpdateWrapper<Dish> wrapper=new LambdaUpdateWrapper<>();
        wrapper.in(Dish::getId,ids);
        Dish dish = new Dish();
        dish.setStatus(1);
        dishMapper.update(dish,wrapper);
        return R.success("起售成功");
    }

    @Override
    public R updown(Long[] ids) {
        //验证参数
        if(ids==null){
            R.error("参数异常");
        }
        //更具菜品id修改状态
        LambdaUpdateWrapper<Dish> wrapper=new LambdaUpdateWrapper<>();
        wrapper.in(Dish::getId,ids);
        Dish dish = new Dish();
        dish.setStatus(0);
        dishMapper.update(dish,wrapper);
        return R.success("停售成功");
    }
    /**
     * 更具分类id查询菜品信息
     */
    @Override
    public R findListBycategoryId(String categoryId) {
        if(categoryId==null){
            return R.error("参数异常");
        }
        //判断缓存中是否有数据
        String key = "dish_"+categoryId+"_1";
        List dishDtos = (List) redisTemplate.opsForValue().get(key);
        //如果没有走数据库 并保存到缓存
        if(dishDtos==null){
            LambdaQueryWrapper<Dish> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(Dish::getCategoryId, categoryId);
            List<Dish> dishes = dishMapper.selectList(wrapper);
            dishDtos = new ArrayList<>();
            //把口味也加上
            for (Dish dish : dishes) {
                //Long id = dish.getId();
                LambdaQueryWrapper<DishFlavor> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(DishFlavor::getDishId,dish.getId());
                List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(wrapper2);
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(dish,dishDto);
                dishDto.setFlavors(dishFlavors);
                dishDtos.add(dishDto);
            }
            redisTemplate.opsForValue().set(key,dishDtos);
        }
        //直接走缓存
        return R.success(dishDtos);
    }
}


