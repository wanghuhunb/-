package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.config.BaseContest;
import com.itheima.entity.AddressBook;
import com.itheima.entity.R;
import com.itheima.entity.User;
import com.itheima.entity.dto.UserDto;
import com.itheima.mapper.AddressBookMapper;
import com.itheima.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地址
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**\
     * 查找所有的地址
     * @return
     */
    @Override
    public R findAll() {
        long id = BaseContest.getId();
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,id);
        List<AddressBook> addressBooks = addressBookMapper.selectList(wrapper);
        return R.success(addressBooks);
    }

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @Override
    public R addAddress(AddressBook addressBook) {
        /**
         * 校验参数
         */
        if(addressBook==null){
            return R.error("参数异常");
        }
        long id = BaseContest.getId();
        addressBook.setUserId(id);
        addressBookMapper.insert(addressBook);
        return R.success("添加成功");
    }

    /**
     * 修改默认地址
     * @param userDto
     */
    @Override
    public R modifyAddressDefault(UserDto userDto) {
        long id = BaseContest.getId();
        //先将所有地址默认值设置为0
        LambdaUpdateWrapper<AddressBook> wrapper =new LambdaUpdateWrapper<>();
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(0);
        wrapper.eq(AddressBook::getUserId,id);
        addressBookMapper.update(addressBook,null);
        //然后将指定id设置为1
        addressBook.setIsDefault(1);
        LambdaUpdateWrapper<AddressBook> wrapper1 =new LambdaUpdateWrapper<>();
        wrapper1.eq(AddressBook::getId,userDto.getId());
        wrapper1.eq(AddressBook::getUserId,id);
        addressBookMapper.update(addressBook,wrapper1);
        return R.success("修改成功");
    }

}
