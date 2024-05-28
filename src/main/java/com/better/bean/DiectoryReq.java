package com.better.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : DiectoryReq
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/7/22 9:17 AM
 * *
 * @Last Update : 2022/7/22 9:17 AM
 * *
 * @Description : 添加directory请求对象
 * *
 * ----------------------------------------------------------------------------------------------
 */
@Data
public class DiectoryReq implements Serializable {


    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;


}
