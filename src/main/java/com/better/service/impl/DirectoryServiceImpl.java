package com.better.service.impl;



import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.better.bean.*;
import com.better.enums.DirectoryTypeEnum;
import com.better.mapper.ArticleMapper;
import com.better.mapper.DirectoryMapper;
import com.better.service.DirectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DirectoryServiceImpl implements DirectoryService {


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private DirectoryMapper directoryMapper;


    /**
     * 目录树展示应该
     *
     * @param
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.asiainfo.ahweb.common.vo.directory.DirectoryVo>
     * @throws
     * @waring NULL
     * @author SL
     * @date 2022/9/2 3:51 PM
     */
    @Override
    public List<DirectoryTreeVo> listDirectory() {
        List<DirectoryTreeVo> list = selectDirectoryTree(Boolean.FALSE);
        return list;
    }

    @Override
    public Directory insertDirectory(DiectoryReq diectoryReq) {
        Directory directory = new Directory();
        BeanUtils.copyProperties(diectoryReq, directory);
        int insert = directoryMapper.insert(directory);
        if (insert > 0) {
            return directory;
        }
        return null;
    }

    @Override
    public Directory updateDirectory(DiectoryReq diectoryReq) {
        Directory directory = new Directory();
        BeanUtils.copyProperties(diectoryReq, directory);
        int update = directoryMapper.updateById(directory);
        if (update > 0) {
            return directory;
        }
        return null;
    }

    @Override
    public Boolean deleteDirectory(ListVoParam listVoParam) {
        if (CollectionUtil.isEmpty(listVoParam.getIdList())) {
            return Boolean.TRUE;
        }
        List<Integer> idList = listVoParam.getIdList();
        for (Integer integer : idList) {
            directoryMapper.deleteById(integer);
        }
        return Boolean.TRUE;
    }


    /**
     * 根据目录id查询父级所有目录结构. 并且需要按照父类在前的顺序排放
     *
     * @param id
     * @waring NULL
     * @return java.util.List<java.lang.String>
     * @author SL
     * @date 2022/7/15 2:44 PM
     */
    @Override
    public List<String> selectArticleDirectory(Integer id) {

        Directory directory = directoryMapper.selectById(id);
        if (directory == null) {
            return new ArrayList<>();
        }

        List<String> list = new ArrayList<>();
        list.add(directory.getName());

        findParentDirectory(directory.getParentId(), list);

        // 反转 将大目录放在前面
        Collections.reverse(list);

        return list;
    }

    /**
     * 根据父级ID获取个层级目录结构
     *      不断根据parentid获取下级目录结构
     *      当目录数据为空时,表明此目录结构到达底部,
     *
     * @param parentId
     * @return java.util.List<com.asiainfo.ahweb.common.vo.directory.DirectoryVo>
     * @waring NULL
     * @author SL
     * @date 2022/7/15 5:06 PM
     */
    @Override
    public DirectoryArticleVo selectDirectoryArticle(Integer parentId) {

        DirectoryArticleVo directoryArticleVo = new DirectoryArticleVo();

        List<Directory> directoryList = directoryMapper.selectDirectoryByParentId(parentId);

        List<DirectoryVo> directoryVos = directoryList.stream().map(item -> {
            DirectoryVo directoryVo = new DirectoryVo();
            BeanUtil.copyProperties(item, directoryVo);
            return directoryVo;
        }).collect(Collectors.toList());


        // 添加当前目录下的文章
        List<Article> articles =  articleMapper.selectByDirectoryId(parentId);

        List<ArticleVo> articleVos = articles.stream().map(item -> {
            ArticleVo articleVo = new ArticleVo();
            BeanUtil.copyProperties(item, articleVo);
            return articleVo;
        }).collect(Collectors.toList());

        directoryArticleVo.setArticleList(articleVos);

        directoryArticleVo.setDirectoryList(directoryVos);

        return directoryArticleVo;
    }


    /**
     * 获取目录结构树
     *
     * @param
     * @param addArticle
     * @return com.asiainfo.ahweb.common.vo.directory.DirectoryTree
     * @waring NULL
     * @author SL
     * @date 2022/7/26 8:55 PM
     */
    @Override
    public List<DirectoryTreeVo> selectDirectoryTree(Boolean addArticle) {

        // 所有目录
        List<Directory> directoryList = directoryMapper.selectAll();
        // 所有文章
        List<Article> articleList = articleMapper.selectAllNoContent();

        List<ArticleTreeVo> articleTreeVos = articleList.stream().map(item -> ArticleTreeVo.builder().id(item.getId())
                .directoryId(item.getDirectoryId())
                .sortNum(item.getSortNum())
                .name(item.getTitle())
                .url(item.getUrl()).build()).collect(Collectors.toList());

        List<DirectoryTreeVo> list = directoryList.stream().map(item -> {
            DirectoryTreeVo directoryTreeVo = new DirectoryTreeVo();
            BeanUtil.copyProperties(item, directoryTreeVo);
            return directoryTreeVo;
        }).collect(Collectors.toList());

        // 目录树构建
        List<DirectoryTreeVo> collect = list.stream()
                .filter(menu -> menu.getParentId() == null)
                .map((menu) -> {
                    List<DirectoryTreeVo> children = getChildren(menu, list, articleTreeVos, addArticle);
                    menu.setChildList(children);
                    return menu;
                }
        ).collect(Collectors.toList());

        return collect;
    }


    /**
     * 递归查询子节点
     *
     * @param rootMenu        根节点
     * @param all         所有节点
     * @param articleList 所有文章
     * @param addArticle  是否添加文章
     * @return 根节点信息
     */
    private List<DirectoryTreeVo> getChildren(DirectoryTreeVo rootMenu, List<DirectoryTreeVo> all, List<ArticleTreeVo> articleList, Boolean addArticle) {

        List<DirectoryTreeVo> children = all.stream().filter(m -> Objects.equals(m.getParentId(), rootMenu.getId())).map((menu) -> {

                    List<DirectoryTreeVo> directoryTreeVos = getChildren(menu, all, articleList, addArticle);

                    menu.setTypeEnum(DirectoryTypeEnum.MENU);
                    menu.setChildList(directoryTreeVos);

                    if (addArticle) {
                        // 添加文章
                        List<ArticleTreeVo> list = getArticleList(menu.getId(), articleList);
                        for (ArticleTreeVo article : list) {
                            DirectoryTreeVo directoryTreeVo = new DirectoryTreeVo();
                            directoryTreeVo.setName(article.getName());
                            directoryTreeVo.setId(article.getId());
                            directoryTreeVo.setTypeEnum(DirectoryTypeEnum.ARTICLE);
                            directoryTreeVo.setParentId(article.getDirectoryId());
                            directoryTreeVo.setSortNum(article.getSortNum());
                            directoryTreeVo.setUrl(article.getUrl());
                            directoryTreeVo.setArticleId(article.getId());

                            // 2023年3月2日 需求 如果文章和目录同名, 那么将此文章标题移除, 设置目录为both,访问当前文章.
                            if (menu.getName().equals(article.getName())){
                                menu.setTypeEnum(DirectoryTypeEnum.MENUARTICLE);
                                menu.setArticleId(article.getId());
                                menu.setUrl(article.getUrl());

                                continue;
                            }

                            directoryTreeVos.add(directoryTreeVo);
                        }




                    }
                    directoryTreeVos.sort((((o1, o2) -> o2.getSortNum().compareTo(o1.getSortNum()))));

                    return menu;
                }
        ).collect(Collectors.toList());

        return children;
    }


    /**
     * 获取子目录
     *
     * @return java.util.List<com.asiainfo.ahweb.common.vo.directory.DirectoryTreeVo>
     * @waring NULL
     * @author SL
     * @date 2022/7/26 9:30 PM
     */
    private List<DirectoryTreeVo> buildTree(List<DirectoryTreeVo> rootList, List<ArticleTreeVo> articleList, Integer id) {
        List<DirectoryTreeVo> treeList = new ArrayList<>();
        rootList.forEach(item -> {
            List<DirectoryTreeVo> buildTree = new ArrayList<>();
            if (id == item.getParentId()) {
                buildTree = buildTree(rootList, articleList, item.getId());
                item.setChildList(buildTree);
                item.setTypeEnum(DirectoryTypeEnum.MENU);
                treeList.add(item);
            }

            // 添加文章
            List<ArticleTreeVo> articleTreeVoList = getArticleList(item.getId(),articleList);
            for (ArticleTreeVo article : articleTreeVoList) {
                DirectoryTreeVo directoryTreeVo = new DirectoryTreeVo();
                directoryTreeVo.setName(article.getName());
                directoryTreeVo.setId(article.getId());
                directoryTreeVo.setTypeEnum(DirectoryTypeEnum.ARTICLE);
                directoryTreeVo.setParentId(article.getDirectoryId());
                directoryTreeVo.setSortNum(article.getSortNum());
                buildTree.add(directoryTreeVo);
            }
            buildTree.sort((((o1, o2) -> o2.getSortNum().compareTo(o1.getSortNum()))));

        });
        return treeList;
    }

    private List<ArticleTreeVo> getArticleList(Integer menuId, List<ArticleTreeVo> articleList) {
        List<ArticleTreeVo> collect = articleList.stream().filter(item -> item.getDirectoryId().equals(menuId)).collect(Collectors.toList());
        return collect;
    }


    /**
     * 递归找到最顶层目录结构
     *
     * @param parentID
     * @param list
     */
    private void findParentDirectory(Integer parentID, List<String> list) {
        if (parentID == null) {
            return;
        }
        Directory directory = directoryMapper.selectById(parentID);
        list.add(directory.getName());
        findParentDirectory(directory.getParentId(), list);
    }


}
