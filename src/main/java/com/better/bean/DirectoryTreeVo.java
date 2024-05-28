package com.better.bean;


import com.better.enums.DirectoryTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : DirectoryTree
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/7/26 8:33 PM
 * *
 * @Last Update : 2022/7/26 8:33 PM
 * *
 * @Description : 目录树结构
 * *
 * ----------------------------------------------------------------------------------------------
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DirectoryTreeVo implements Serializable {


    private Integer id;

    private Integer parentId;

    private String name;

    private DirectoryTypeEnum typeEnum;

    private Long sortNum;

    private String url;

    /**
     * 仅当menu与article同名时使用
     */
    private Integer articleId;


    private List<DirectoryTreeVo> childList = new ArrayList<>();
}
