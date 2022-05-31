package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.Employee;
import com.itheima.entity.R;
import com.itheima.entity.dto.LoginDto;
import com.itheima.entity.dto.PageDto;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户的服务接口
 */
@Transactional
public interface EmployeeService extends IService<Employee> {
    /**
     * 登陆方法
     *
     * @param loginDto
     * @return
     */
    public R login(LoginDto loginDto);

    /**
     * 添加员工方法
     *
     * @param employee
     * @return
     */
    R add(Employee employee);

    /**
     * 分页
     */
    R page(PageDto pageDto);

    R checkStutas(Employee employee);

    /**
     * 修改回显
     *
     * @return
     */
    R findById(Long id);
}
