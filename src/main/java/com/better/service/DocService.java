package com.better.service;

import com.better.bean.ArticleSearchParam;
import com.better.bean.ArticleVo;
import com.better.bean.DirectoryTreeVo;
import com.better.bean.PageVo;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @Author: wanwgei70
 * @Description:
 * @Date Created at 10:36 2024/5/7
 * @Modified By:
 */
public interface DocService {
    /**
     *
     * @param dirFilePath
     * @param version
     * @param productId parent_id为null的那一列（产品）
     * @return
     */
    String importDirectory(String dirFilePath , Integer id);

    int importMdDirectory(String directoryPath, Integer directoryId);

    int importMdFile(String filePath, Integer directoryId);

    // 查询目录树
    public List<DirectoryTreeVo> selectDirectoryTree(Boolean addArticle);


    /**
     * 全文检索
     *
     * @param pageVo
     * @waring NULL
     * @return void
     * @author SL
     * @date 2022/7/12 2:54 PM
     */
    Page<ArticleVo> listArticle(ArticleSearchParam articleSearchParam);


    /**
     * 文章详情
     *
     * @param id
     * @waring NULL
     * @return com.asiainfo.ahweb.common.vo.article.ArticleVo
     * @author SL
     * @date 2022/7/15 10:28 AM
     */
    ArticleVo detailArticle(Integer id);

    void download(HttpServletResponse response) throws IOException;
}
