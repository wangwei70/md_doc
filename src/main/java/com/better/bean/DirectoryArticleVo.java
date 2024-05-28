package com.better.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : DirectoryArticleVo
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/7/21 3:10 PM
 * *
 * @Last Update : 2022/7/21 3:10 PM
 * *
 * @Description : 前端展示vo  同级结构中包含目录和文章结构
 * *
 * ----------------------------------------------------------------------------------------------
 */
@Data
public class DirectoryArticleVo implements Serializable {

    /**
     * 目录结构
     */
    private List<DirectoryVo> directoryList;


    /**
     * 文章数据
     */
    private List<ArticleVo> articleList;



}
