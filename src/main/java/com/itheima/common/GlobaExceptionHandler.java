package com.itheima.common;

import com.itheima.entity.R;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobaExceptionHandler {
    //所有的异常
    @ExceptionHandler(Exception.class)
    public R exctption(Exception ex){
        ex.printStackTrace();
        return R.error("服务器异常,请稍后再试......");
    }
    //添加用户异常
    @ExceptionHandler(DuplicateKeyException.class)
    public R exctption2(DuplicateKeyException ex){
        ex.printStackTrace();
        return R.error("用户名已存在");
    }
}
