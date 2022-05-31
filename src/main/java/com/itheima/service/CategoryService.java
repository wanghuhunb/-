package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.Category;
import com.itheima.entity.R;
import com.itheima.entity.dto.PageDto;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分类管理
 */
@Transactional
public interface CategoryService extends IService<Category> {
    //添加方法
    R add(Category category);

    R pageInfo(PageDto pageDto);

    R delete(Long id);

    R findAll(String type);
}
