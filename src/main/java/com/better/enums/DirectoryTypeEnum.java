package com.better.enums;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : VersionType
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/7/15 11:01 AM
 * *
 * @Last Update : 2022/7/15 11:01 AM
 * *
 * @Description : TODO
 * *
 * ----------------------------------------------------------------------------------------------
 */
public enum DirectoryTypeEnum {


    MENU(1,"目录"),
    ARTICLE(2,"文章"),

    /**
     *  2023年3月2日
     *
     *  如果文章和目录同名, 那么将此文章标题移除, 设置目录为both,访问当前同名文章.
     */
    MENUARTICLE(3,"目录与文章同名")

    ;

    private Integer type;

    private String description;

    DirectoryTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }



    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
