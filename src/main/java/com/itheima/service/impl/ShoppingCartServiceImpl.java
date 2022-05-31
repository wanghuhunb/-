package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.itheima.config.BaseContest;
import com.itheima.entity.R;
import com.itheima.entity.ShoppingCart;
import com.itheima.entity.dto.DishDto;
import com.itheima.mapper.ShoppingCartMapper;
import com.itheima.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public R findAll() {
        long id = BaseContest.getId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, id);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(wrapper);
        return R.success(shoppingCarts);
    }

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @Override
    public R add(ShoppingCart shoppingCart) {
        //补全参数
        long id = BaseContest.getId();
        shoppingCart.setUserId(id);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, id);
        //判断你要加的是套餐还是菜品
        if (shoppingCart.getDishId() != null) {
            //表示你要添加菜品
            wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartMapper.selectOne(wrapper);
        //判断数据库中是否存在这个菜,不存在新建 默认数据为一
        if (shoppingCart1 != null) {
            //存在数量加一
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartMapper.updateById(shoppingCart1);
            return R.success(shoppingCart1);
        }
        //不存在新建 默认数据为一
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.insert(shoppingCart);
        return R.success(shoppingCart);
    }

    /**
     * 清空购物车
     *
     * @param id
     * @return
     */
    @Override
    public R cleanById(long id) {
        LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, id);
        shoppingCartMapper.delete(wrapper);
        return R.success("删除成功");
    }

    /**
     * 修改购物车数量
     *
     * @param dishDto
     * @return
     */
    @Override
    public R sub(DishDto dishDto) {
        //判断是菜品还是套餐
        long id = BaseContest.getId();
        if(dishDto.getDishId()!=null){
            //判断菜品的数量是多少
            LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ShoppingCart::getDishId,dishDto.getDishId());
            wrapper.eq(ShoppingCart::getUserId,id);
            ShoppingCart shoppingCart = shoppingCartMapper.selectOne(wrapper);
            if (shoppingCart.getNumber()>1){
                //大于一修改数量
                shoppingCart.setNumber(shoppingCart.getNumber()-1);
                shoppingCartMapper.update(shoppingCart,wrapper);

            }else {
                //小于1删除
                shoppingCartMapper.delete(wrapper);
            }
            return R.success("修改成功");
        }else {
            //判断菜品的数量是多少
            LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(ShoppingCart::getSetmealId,dishDto.getSetmealId());
            wrapper.eq(ShoppingCart::getUserId,id);
            ShoppingCart shoppingCart = shoppingCartMapper.selectOne(wrapper);
            if (shoppingCart.getNumber()>1){
                //大于一修改数量
                shoppingCart.setNumber(shoppingCart.getNumber()-1);
                shoppingCartMapper.update(shoppingCart,wrapper);

            }else {
                //小于1删除
                shoppingCartMapper.delete(wrapper);
            }
            return R.success("修改成功");
        }

    }
}
