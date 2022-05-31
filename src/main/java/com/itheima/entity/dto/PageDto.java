package com.itheima.entity.dto;

import lombok.Data;

/**
 * 分页参数
 */
@Data
public class PageDto {
    private Integer page;
    private Integer pageSize;
    private String name;
}
