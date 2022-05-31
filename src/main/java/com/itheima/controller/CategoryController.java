package com.itheima.controller;

import com.itheima.entity.Category;
import com.itheima.entity.R;
import com.itheima.entity.dto.PageDto;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加分类
     */
    @PostMapping
    public R add(@RequestBody Category category) {
        return categoryService.add(category);
    }

    /**
     * 分页查找
     *
     * @param pageDto
     * @return
     */
    @GetMapping("/page")
    public R page(PageDto pageDto) {
        return categoryService.pageInfo(pageDto);
    }

    /**
     * 删除分类
     */
    @DeleteMapping()
    public R delete(Long id) {
        return categoryService.delete(id);
    }
    /**
     * 修改分类
     */
    @PutMapping()
    public R modify(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    /**
     * 查需所有的分类(添加菜品用)
     */
    @GetMapping("list")
    private R findAll(String type){
      return  categoryService.findAll(type);
    }
}
