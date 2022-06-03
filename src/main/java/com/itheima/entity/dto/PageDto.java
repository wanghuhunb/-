package com.itheima.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分页参数
 */
@Data
public class PageDto implements Serializable {
    private Integer page;
    private Integer pageSize;
    private String name;
    //订单号
    private String number;
    //开始时间
    private String beginTime;
    //结束时间
    private String endTime;

}
