package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.R;
import com.itheima.entity.Setmeal;
import com.itheima.entity.dto.PageDto;
import com.itheima.mapper.CategoryMapper;
import com.itheima.mapper.DishMapper;
import com.itheima.mapper.SetmealMapper;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类管理
 */
@Service
public class CategoryServiceimpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public R add(Category category) {
        if (category == null) {
            return R.error("参数异常");
        }
        int insert = categoryMapper.insert(category);
        return R.success("添加成功");
    }

    @Override
    public R pageInfo(PageDto pageDto) {
        if (pageDto == null) {
            return R.error("参数异常");
        }
        Page<Category> page = new Page<>(pageDto.getPage(), pageDto.getPageSize());
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        Page<Category> categoryPage = categoryMapper.selectPage(page, wrapper);
        return R.success(categoryPage);
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @Override
    public R delete(Long id) {
        //判断这个分类下面有没有菜品
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);
        Integer integer = dishMapper.selectCount(wrapper);
        if (integer > 0) {
            return R.error("此分类存在菜品,不能删除");
        }
        //判断这个分类下面有没有套餐
        LambdaQueryWrapper<Setmeal> wrapperSetmeal = new LambdaQueryWrapper<>();
        wrapperSetmeal.eq(Setmeal::getCategoryId, id);
        Integer integer1 = setmealMapper.selectCount(wrapperSetmeal);
        if (integer1 > 0) {
            return R.error("此分类存在套餐,不能删除");
        }
        //删除
        int i = categoryMapper.deleteById(id);
        return R.success("删除成功");
    }

    @Override
    public R findAll(String type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if(type!=null){
            wrapper.eq(Category::getType,type);
            List<Category> categories = categoryMapper.selectList(wrapper);
            return R.success(categories);
        }
        List<Category> categories = categoryMapper.selectList(null);
        return R.success(categories);
    }
}
