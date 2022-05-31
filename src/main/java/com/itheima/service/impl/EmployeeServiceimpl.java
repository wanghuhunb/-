package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.Employee;
import com.itheima.entity.R;
import com.itheima.entity.dto.LoginDto;
import com.itheima.entity.dto.PageDto;
import com.itheima.mapper.EmployeeMapper;
import com.itheima.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class EmployeeServiceimpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    //注入用户的服务接口
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 登陆服务
     *
     * @param loginDto
     * @return
     */
    @Override
    public R login(LoginDto loginDto) {
        //1,将密码进行md5加密
        String password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        //2,根据账号查询是否催在这个人
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, loginDto.getUsername());
        //获取一个员工对象
        Employee employee = employeeMapper.selectOne(wrapper);
        if (employee == null) {
            return R.error("员工不存在");
        }
        //3,员工账号存在匹配密码
        if (!password.equals(employee.getPassword())) {
            return R.error("密码错误");
        }
        //4,判断是否禁用
        if (employee.getStatus() == 0) {
            return R.error("员工被禁用");
        }
        return R.success(employee);

    }

    /**
     * 添加员工方法
     *
     * @param employee
     * @return
     */
    @Override
    public R add(Employee employee) {
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(password);
        employee.setStatus(1);
        int insert = employeeMapper.insert(employee);
        return R.success("新增成功");
    }

    /**
     * 分页功能
     *
     * @param pageDto
     * @return
     */
    @Override
    public R page(PageDto pageDto) {
        Page page = new Page(pageDto.getPage(), pageDto.getPageSize());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
        if (pageDto.getName() != null && pageDto.getName() != "") {
            wrapper.like(Employee::getName, pageDto.getName());
        }
        wrapper.orderByDesc(Employee::getUpdateTime);
        Page page1 = employeeMapper.selectPage(page, wrapper);
        return R.success(page1);
    }

    /**
     * 修改状态
     *
     * @param employee
     * @return
     */
    @Override
    public R checkStutas(Employee employee) {
        if (employee == null) {
            return R.error("参数异常");
        }
        int i = employeeMapper.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 修改数据回显方法
     *
     * @param id
     * @return
     */
    @Override
    public R findById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        return R.success(employee);
    }

}
