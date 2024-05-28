package com.better.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : Page
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/7/12 5:09 PM
 * *
 * @Last Update : 2022/7/12 5:09 PM
 * *
 * @Description : TODO
 * *
 * ----------------------------------------------------------------------------------------------
 */
@Data
public class PageVo implements Serializable {

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 大小
     */
    private Integer pageSize;


}
