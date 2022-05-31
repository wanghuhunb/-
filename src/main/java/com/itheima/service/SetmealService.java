package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.R;
import com.itheima.entity.Setmeal;
import com.itheima.entity.dto.PageDto;
import com.itheima.entity.dto.SetmealDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 套餐接口
 */
@Transactional
public interface SetmealService extends IService<Setmeal> {
    R addSetmeal(SetmealDto dishDto);

    R fandPage(PageDto pageDto);

    R deleteByids(List<Long> ids);

    R fingByOne(Long id);

    R modify(SetmealDto setmealDto);

    R startAndEnd(Integer id, Long[] ids);

    R fandList(Setmeal setmeal);
}
