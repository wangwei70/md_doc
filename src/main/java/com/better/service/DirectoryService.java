package com.better.service;


import com.better.bean.*;

import java.util.List;

/**
 * 目录结构
 *
 * @waring NULL
 * @return
 * @author SL
 * @date 2022/7/12 4:48 PM
 */
public interface DirectoryService {


    /**
     * 分页获取目录结构(admin)
     *
     * @param
     * @return void
     * @waring NULL
     * @author SL
     * @date 2022/7/12 4:53 PM
     */
    List<DirectoryTreeVo> listDirectory();

    /**
     * 添加目录
     *
     * @param
     * @param diectoryReq
     * @return void
     * @waring NULL
     * @author SL
     * @date 2022/7/12 4:53 PM
     */
    Directory insertDirectory(DiectoryReq diectoryReq);


    /**
     * 删除目录
     *
     * @param
     * @param diectoryReq
     * @return void
     * @waring NULL
     * @author SL
     * @date 2022/7/12 4:53 PM
     */
    Directory updateDirectory(DiectoryReq diectoryReq);


    /**
     * 删除文档
     *
     * @param
     * @param listVoParam
     * @return void
     * @waring NULL
     * @author SL
     * @date 2022/7/12 4:53 PM
     */
    Boolean deleteDirectory(ListVoParam listVoParam);


    /**
     * 根据id获取目录结构,顺序
     *
     * @param id
     * @waring NULL
     * @return java.util.List<java.lang.String>
     * @author SL
     * @date 2022/7/15 11:38 AM
     */
    List<String> selectArticleDirectory(Integer id);


    /**
     * 根据版本号获取对应目录信息
     *
     * @param
     * @param parentId
     * @return java.util.List<com.asiainfo.ahweb.common.vo.directory.DirectoryVo>
     * @waring NULL
     * @author SL
     * @date 2022/7/15 5:04 PM
     */
    DirectoryArticleVo selectDirectoryArticle(Integer parentId);

    /**
     * 目录树获取
     *
     * @param addArticle 是否添加文章目录
     * @return java.util.List<com.asiainfo.ahweb.common.vo.directory.DirectoryTreeVo>
     * @throws
     * @waring NULL
     * @author SL
     * @date 2022/9/2 4:37 PM
     */
    List<DirectoryTreeVo> selectDirectoryTree(Boolean addArticle);
}
