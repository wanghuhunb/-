package com.itheima.config;

import javax.lang.model.element.VariableElement;

/**
 * 线程绑定对象
 */
public class BaseContest {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    public static void setId(long id) {
        threadLocal.set(id);
    }

    public static long getId() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
