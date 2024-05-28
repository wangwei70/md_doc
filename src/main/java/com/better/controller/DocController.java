package com.better.controller;


import com.better.bean.ApiResult;
import com.better.bean.ArticleSearchParam;
import com.better.bean.ArticleVo;
import com.better.bean.DirectoryTreeVo;
import com.better.service.DirectoryService;
import com.better.service.DocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RestController
@Tag(name = "目录获取",description = "获取用户端目录结构")
@RequestMapping("/doc")
public class DocController {


    @Autowired
    private DocService docService;

    /**
     * 获取整颗目录树结构
     *
     * @param
     * @waring NULL
     * @return com.asiainfo.ahweb.config.ApiResult<com.asiainfo.ahweb.common.vo.directory.DirectoryArticleVo>
     * @author SL
     * @date 2022/7/26 8:30 PM
     */
    @GetMapping("/list")
    @Operation(summary = "获取目录结构树")
    public ApiResult<List<DirectoryTreeVo>> selectDirectoryTree(){
        return ApiResult.ok(docService.selectDirectoryTree(Boolean.TRUE));
    }


    /**
     * 获取文档分页查询
     *
     * @param articleSearchParam
     * @waring NULL
     * @return com.asiainfo.ahweb.config.ApiResult<org.springframework.data.domain.Page < com.asiainfo.ahweb.common.vo.article.ArticleVo>>
     * @author SL
     * @date 2022/7/18 5:08 PM
     */
    @PostMapping("/page/list")
    @Operation(summary = "分页查询")
    public ApiResult<Page<ArticleVo>> listArticle(@RequestBody ArticleSearchParam articleSearchParam){

        return ApiResult.ok(docService.listArticle(articleSearchParam));

    }

    /**
     * 文章详情
     *
     * @param id
     * @waring NULL
     * @return com.asiainfo.ahweb.config.ApiResult<com.asiainfo.ahweb.common.vo.article.ArticleVo>
     * @author SL
     * @date 2022/7/20 5:14 PM
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "根据ID获取详情")
    public ApiResult<ArticleVo> detailArticle(@PathVariable("id") Integer id){

        return ApiResult.ok(docService.detailArticle(id));
    }


    // todo 下载接口
    @GetMapping("/download")
    @Operation(summary = "根据ID获取详情")
    public ApiResult<ArticleVo> download(HttpServletResponse response) throws IOException {
        docService.download(response);
        return ApiResult.ok();

    }


}
