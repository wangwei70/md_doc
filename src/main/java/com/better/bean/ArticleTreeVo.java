package com.better.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : ArticleTreeVO
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/7/26 9:11 PM
 * *
 * @Last Update : 2022/7/26 9:11 PM
 * *
 * @Description : 目录树文章vo
 * *
 * ----------------------------------------------------------------------------------------------
 */
@Data
@Builder
@ToString
public class ArticleTreeVo implements Serializable {

    private Integer id;

    private String name;

    private Long sortNum;

    private Integer directoryId;

    private String url;

}
