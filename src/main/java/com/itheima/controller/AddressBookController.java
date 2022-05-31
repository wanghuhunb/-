package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.config.BaseContest;
import com.itheima.entity.AddressBook;
import com.itheima.entity.R;
import com.itheima.entity.User;
import com.itheima.entity.dto.UserDto;
import com.itheima.service.AddressBookService;
import org.apache.tomcat.jni.Address;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 地址相关服务
 */
@RestController
@RequestMapping("addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查找所有地址
     *
     * @return
     */
    @GetMapping("/list")
    public R findAll() {
        return addressBookService.findAll();
    }

    /**
     * 添加地址
     */
    @PostMapping()
    public R addAddress(@RequestBody AddressBook addressBook) {
        return addressBookService.addAddress(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    public R modifyAddressDefault(@RequestBody UserDto userDto) {
        return addressBookService.modifyAddressDefault(userDto);
    }
    /**
     * 数据回显
     */
    @GetMapping("/{id}")
    public R fingByid(@PathVariable("id") Long id){
        AddressBook byId = addressBookService.getById(id);
        return R.success(byId);
    }
    /**
     * 修改地址
     */
    @PutMapping()
    public R modify(@RequestBody AddressBook addressBook){
        boolean b = addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }
    /**
     * 删除地址
     */
    @DeleteMapping
    public R deleteByid(Long ids){
        boolean b = addressBookService.removeById(ids);
        return R.success("删除成功");
    }
    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public R getdefault(){
        long id = BaseContest.getId();
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,id);
        wrapper.eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(wrapper);
        if (one==null){
            return R.error("没有找到改对象");
        }else {
            return R.success(one);
        }
    }

}
