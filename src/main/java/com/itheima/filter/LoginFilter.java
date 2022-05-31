package com.itheima.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.config.BaseContest;
import com.itheima.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录的过滤器
 */
@Slf4j
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    //定义一个路径拦截器
    private static final AntPathMatcher apm = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        System.out.println("锅炉其");
        //进行强转
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取请求的路径
        String requestURI = request.getRequestURI();
        //定义一个放行路径数组
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/**",
                "/common/**"
        };
        //包含路径放行
        if (check(urls, requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        //判断是否登录
        HttpSession session = request.getSession();
        Object employee = session.getAttribute("employee");
        Object userId = session.getAttribute("userId");
        if (employee != null) {
            BaseContest.setId((Long) employee);
            chain.doFilter(request, response);
            return;
        }
        //将id放入线程域当中
        if (userId != null) {
            BaseContest.setId((Long) userId);
            chain.doFilter(request, response);
            return;
        }
        //最后拦截
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 创建一个匹配方法 判单是否需要放行
     *
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = apm.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

}
