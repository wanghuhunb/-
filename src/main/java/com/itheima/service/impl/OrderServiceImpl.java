package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
;
import com.itheima.config.BaseContest;
import com.itheima.entity.*;
import com.itheima.mapper.AddressBookMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.mapper.ShoppingCartMapper;
import com.itheima.mapper.UserMapper;
import com.itheima.service.OrderDetailService;
import com.itheima.service.OrderService;
import com.itheima.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public R submit(Orders orders) {
        //
        if (orders == null) {
            return R.error("参数异常");
        }
        long id = BaseContest.getId();

        LambdaQueryWrapper<ShoppingCart> scwrapper = new LambdaQueryWrapper<>();
        scwrapper.eq(ShoppingCart::getUserId, id);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(scwrapper);
        if (shoppingCarts.size() == 0) {
            return R.error("购物车为空");
        }
        AddressBook addressBook = addressBookMapper.selectById(orders.getAddressBookId());
        if (addressBook == null) {
            return R.error("没有地址");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            return R.error("没有这个用户");
        }
        //把,购物车转换成为订明细表数据
        long id1 = IdWorker.getId();
        AtomicInteger count = new AtomicInteger(1);//及总个数
        AtomicInteger amont = new AtomicInteger();   //记录总金额
        List<OrderDetail> orderDetailList = shoppingCarts.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setOrderId(id1);
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setImage(shoppingCart.getImage());
            //进行累加
            count.addAndGet(shoppingCart.getNumber());
            amont.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber() + "")).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        orders.setId(id1);
        orders.setNumber(UUID.randomUUID().toString());
        orders.setOrderTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amont.get()+""));
        orders.setPhone(addressBook.getPhone());
        orders.setUserName(user.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        String addressINfo=addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail();
        orders.setAddress(addressINfo);
        orders.setUserId(id);
        //
        orderMapper.insert(orders);
        orderDetailService.saveBatch(orderDetailList);
        //下单结束清空购物车
        shoppingCartService.cleanById(user.getId());
        return R.success(orders);
    }
}