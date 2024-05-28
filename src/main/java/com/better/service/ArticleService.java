package com.better.service;




import com.better.bean.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 在线文档service
 *
 * @waring 最新的方法在最上面
 * @return
 * @author SL
 * @date 2022/7/11 4:29 PM
 */
public interface ArticleService {

    
    
    /**
     * 根据目录id获取文章
     *
     * @param directoryId
     * @waring NULL
     * @return java.util.List<com.asiainfo.ahweb.common.entity.Article>
     * @author SL
     * @date 2022/7/21 4:31 PM   
     */
    List<Article> selectByDirectoryId(Integer directoryId);
    
    /**
     * 全文检索
     *
     * @param pageVo
     * @waring NULL
     * @return void
     * @author SL
     * @date 2022/7/12 2:54 PM
     */
    Page<ArticleVo> listArticle(PageVo pageVo);


    /**
     * 添加文档
     *
     * @param articleParam
     * @param userID
     * @return com.asiainfo.ahweb.common.entity.Article
     * @waring NULL
     * @author SL
     * @date 2022/7/12 2:38 PM
     */
    Article insertArticle(ArticleParam articleParam, Object userID);


    /**
     * 更新文档
     *
     * @param articleParam
     * @param userID
     * @return com.asiainfo.ahweb.common.entity.Article
     * @waring NULL
     * @author SL
     * @date 2022/7/12 2:39 PM
     */
    Article updateArticle(ArticleParam articleParam, Object userID);


    /**
     * 删除文档
     *
     * @param idList
     * @param userID
     * @return java.lang.Boolean
     * @waring NULL
     * @author SL
     * @date 2022/7/12 2:42 PM
     */
    Boolean deleteArticle(ArticleIdListParam idList, Object userID);


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

    /**
     * 全文检索所有文章
     *
     * @param searchContent 搜索内容
     * @waring NULL
     * @return java.util.List<com.asiainfo.ahweb.common.vo.article.ArticleVo>
     * @author SL
     * @date 2022/7/25 9:32 AM
     */
    List<ArticleVo> searchArticleList(String searchContent);

    List<Article> selectAllNoHtmlField();

}
