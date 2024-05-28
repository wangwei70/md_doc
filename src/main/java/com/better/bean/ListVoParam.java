package com.better.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : ListVoParam
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/7/22 9:20 AM
 * *
 * @Last Update : 2022/7/22 9:20 AM
 * *
 * @Description : list vo 用户接收多个id参数
 * *
 * ----------------------------------------------------------------------------------------------
 */
@Data
public class ListVoParam implements Serializable {

    List<Integer> idList;


}
