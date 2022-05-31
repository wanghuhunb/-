package com.itheima.controller;

import com.itheima.entity.Employee;
import com.itheima.entity.R;
import com.itheima.entity.dto.LoginDto;
import com.itheima.entity.dto.PageDto;
import com.itheima.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 员工相关服务类
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    //注入员工服务类
    @Autowired
    private EmployeeService employeeService;
    //注入request
    @Autowired
    private HttpServletRequest request;

    /**
     * 登录功能
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginDto loginDto) {
        //System.out.println(loginDto);
        R r = employeeService.login(loginDto);
        Object data = r.getData();
        //判断是否为空
        if (data != null) {
            //不为空将id存入session
            HttpSession session = request.getSession();
            Employee employee = (Employee) data;
            session.setAttribute("employee", employee.getId());
        }
        return r;
    }

    /**
     * 退出功能
     */
    @PostMapping("/logout")
    public R loginOut() {
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 添加员工
     */
    @PostMapping
    public R add(@RequestBody Employee employee) {
        R add = employeeService.add(employee);
        return add;
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R page(PageDto pageDto) {
        R page = employeeService.page(pageDto);
        return page;
    }

    /**
     * 修改员工状态
     */
    @PutMapping
    public R checkStutas(@RequestBody Employee employee) {

        return employeeService.checkStutas(employee);
    }
    /**
     * 数据回显
     */
    @GetMapping("/{id}")
    public R findId(@PathVariable("id") Long id){
      return   employeeService.findById(id);
    }


}
