package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.AddressBook;
import com.itheima.entity.R;
import com.itheima.entity.dto.UserDto;

public interface AddressBookService  extends IService<AddressBook> {
    R findAll();

    R addAddress(AddressBook addressBook);

    R modifyAddressDefault(UserDto userDto);
}
