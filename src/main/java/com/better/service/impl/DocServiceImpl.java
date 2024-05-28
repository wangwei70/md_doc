package com.better.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.file.FileReader;
import com.better.bean.*;
import com.better.config.DocProperty;
import com.better.enums.DirectoryTypeEnum;
import com.better.mapper.ArticleMapper;
import com.better.mapper.DirectoryMapper;
import com.better.service.ArticleService;
import com.better.service.DocService;
import com.better.util.ClassUtil;
import com.better.util.FileSearchUtils;
import com.better.util.MarkdownUtil;
import com.better.util.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: JeanRiver
 * @Description:
 * @Date Created at 10:37 2022/8/29
 * @Modified By:
 */
@Service
public class DocServiceImpl implements DocService {

    private static final Logger logger = LoggerFactory.getLogger(DocServiceImpl.class);

    @Autowired
    private DirectoryMapper directoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    private static final String compress_suffix = ".zip";

    @Override
    public String importDirectory(String dirFilePath, Integer id) {
        File file = new File(dirFilePath);
        FileReader fileReader = new FileReader(file);
        List<String> strs = fileReader.readLines();
        int i = 0, j = 1;

        List<Directory> directories = new ArrayList<>();
        Directory parDirectory = directoryMapper.selectById(id);
        if (parDirectory == null) {
            parDirectory = new Directory();
            parDirectory.setName(file.getParentFile().getName());
            parDirectory.setCreateTime(new Date());
            parDirectory.setAbsolutePath(dirFilePath);
            parDirectory.setId(id);
            directories.add(parDirectory);
        }


        // 存储父目录ID
        int currentId = id;
        List<Integer> tempParentId = new ArrayList<>();
        tempParentId.add(id);
        for (int k = 0; k < 4; k++) {
            tempParentId.add(0);
        }
        List<Article> articles = new ArrayList<>();
        Long sort_num = 1000L;
        String currentDir = file.getParentFile().getAbsolutePath();
        int preN = 0;
        while (j < strs.size()) {
            // 空格换行不加入
            while (containsSharp(strs.get(i)) == 0 && i < 99) {
                i++;
                j++;
            }
            int in = containsSharp(strs.get(i));
            int jn = containsSharp(strs.get(j));
            // 标题中间包含空格，使用@@标记
            String title = strs.get(i).replace("#", "").replace(" ", "").replace("@@", " ");
            currentDir = refreshCurrentDir(currentDir, preN, in, title);
            if (in < jn) {
                Directory subDir = new Directory();
                subDir.setCreateTime(new Date());
                subDir.setName(title);
                // 数组里获取此级目录当前的parentId;
                subDir.setParentId(tempParentId.get(in - 1));
                subDir.setId(++currentId);
                subDir.setSortNum(sort_num);
                subDir.setAbsolutePath(currentDir);
                // 存储当前的parent值
                tempParentId.set(in, currentId);
                directories.add(subDir);
            } else {
                Article article = new Article();
                article.setDirectoryId(tempParentId.get(in - 1));
                String fileDir = currentDir + ".md";
                article.setTitle(title);
                article.setUserId(0);
                article.setSortNum(sort_num);
                articles.add(article);
                article.setAbsolutePath(fileDir);
            }
            i++;
            j++;
            preN = in;
            sort_num--;
        }
        int d = directoryMapper.insertBatch(directories);
        int a = articleMapper.insertBatch(articles);
        return String.format("一共创建了 %s 个目录， %s篇文章", d, a);
    }

    private String refreshCurrentDir(String currentDir, int preN, int in, String title) {
        if (in > preN) {
            // 进入子目录
            currentDir = currentDir + File.separator + title;
        } else if (in == preN) {
            // 进入同级目录
            currentDir = new File(currentDir).getParentFile().getAbsolutePath() + File.separator + title;
        } else {
            // 退回父目录
            for (int k = 1; k <= preN - in; k++) {
                currentDir = new File(currentDir).getParentFile().getParentFile().getAbsolutePath() + File.separator + title;
            }
        }
        return currentDir;
    }

    private String refreshCurrentDir(Integer in, Integer curN, String currentDir, String subDirName) {

        return currentDir;
    }

    /**
     * @param str 字符串
     * @return 包含#的数量
     */
    private int containsSharp(String str) {
        String sharp = "#";
        return (str.length() - str.replace(sharp, "").length()) / sharp.length();
    }

    @Override
    public int importMdDirectory(String directoryPath, Integer directoryId) {
        //这一步获取到文件夹所有的md对象
        List<File> mdFiles = new ArrayList<>();
        FileSearchUtils.searchBySuffix(directoryPath, ".md", mdFiles);
        for (File mdFile :
                mdFiles) {
            updateFile(mdFile, directoryId);
        }
        return mdFiles.size();
    }

    @Override
    public int importMdFile(String filePath, Integer directoryId) {
        File mdFile = new File(filePath);
        updateFile(mdFile, directoryId);
        return 1;
    }

    private void updateFile(File mdFile, Integer directoryId) {
        String mdParentDir = mdFile.getParentFile().getName();
        logger.info("获取到 {}, 父文件夹名为 {}", mdFile.getName(), mdParentDir);
        List<Directory> directories = directoryMapper.getAllByName(mdParentDir);
        Integer tarDirectoryId;
        if (directoryId != null) {
            tarDirectoryId = directoryId;
        } else if (CollectionUtil.isNotEmpty(directories)) {
            tarDirectoryId = directories.get(0).getId();
        } else {
            return;
        }
        ArticleParam param = generateParam(tarDirectoryId, mdFile);
        if (param.getId() != null) {
            articleService.updateArticle(param, -1);
        }
        // 不再生成新文章
//            else {
//                articleService.insertArticle(param, -1);
//            }

    }

    private ArticleParam generateParam(Integer directoryId, File file) {
        ArticleParam articleParam = new ArticleParam();
        FileReader fileReader = new FileReader(file);
        // 内容转换
        String content = getContent(fileReader, articleParam);
        String markdown2Html = MarkdownUtil.markdown2Html(content);
        articleParam.setHtmlContent(markdown2Html);
        articleParam.setOriginContent(content);
        if (directoryId > 0) {
            articleParam.setDirectoryId(directoryId);
        }
        articleParam.setTitle(file.getName().substring(0, file.getName().lastIndexOf(".")));
        // 查询是否存在同名文档(delete_flag = 0)
        List<Article> exists;
        if (directoryId > 0) {
            exists = articleMapper.selectAllByTitleAndDirectoryId(articleParam.getTitle(), directoryId);
        } else {
            exists = articleMapper.selectAllByTitle(articleParam.getTitle());
        }
        if (CollectionUtil.isNotEmpty(exists)) {
            Integer id = exists.get(0).getId();
            logger.info("发现同title文档, id = {}, directoryId = {},将执行更新操作", id, directoryId);
            articleParam.setId(id);
        }
        return articleParam;
    }


    private String getContent(FileReader fileReader, ArticleParam articleParam) {

        Class<? extends ArticleParam> clazz = articleParam.getClass();

        // 字段名
        List<String> fieldList = Arrays.stream(ClassUtil.fields(ArticleParam.class)).map(field -> field.getName()).collect(Collectors.toList());

        List<String> strings = fileReader.readLines();
        ListIterator<String> stringListIterator = strings.listIterator();

        // 字段长度
        int objectLength = 0;

        // 行
        try {
            while (stringListIterator.hasNext()) {
                if (objectLength == fieldList.size()) {
                    break;
                }
                objectLength += 1;
                String line = stringListIterator.next();
                String[] split = line.split(":");
                if (split.length < 2) {
                    split = line.split("：");
                }
                if (split.length < 2) {
                    continue;
                }
                // 列名
                for (String field : fieldList) {

                    // 行数据包含字段
                    if (split[0].equals(field)) {

                        Field clazzField = ClassUtil.getFiled(field, clazz);
                        clazzField.set(articleParam, split[1]);
                        stringListIterator.remove();
                    }

                }

            }
        } catch (Exception e) {
            logger.error(" field 赋值失败=========={}", e.getMessage());
            return fileReader.readString();
        }
        String join = String.join("\n", strings);
        return join;
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
     * 分页查询，可以根据目录id查询或者根据关键词查询
     *
     * @param param
     * @return
     */
    @Override
    public Page<ArticleVo> listArticle(ArticleSearchParam param) {
        int num = param.getPageNum() == null ? 1 : param.getPageNum();
        int size = param.getPageSize() == null ? 10 : param.getPageSize();

        Pageable pageable = PageRequest.of(num, size, Sort.by("id").descending());

        List<? extends Article> articles = new ArrayList<>();
        if (param.getDirectoryId() != null && param.getDirectoryId() > 0) {
            articles = articleMapper.selectByDirectoryId(param.getDirectoryId());
        } else if (!Objects.isNull(param.getSearch())) {
            articles = articleMapper.selectByKeyword(param.getSearch());
        } else {
            Assert.isTrue(false, "查询参数错误");
        }
        if (CollectionUtil.isEmpty(articles)) {
            return new PageImpl(null,pageable,0);
        }

        List<ArticleVo> articleVoList = articles.stream().map(item -> {
            if (item instanceof ArticleVo) {
                return (ArticleVo) item;
            }
            ArticleVo articleVo = new ArticleVo();
            BeanUtil.copyProperties(item, articleVo);
            return articleVo;
        }).collect(Collectors.toList());

        // 内存分页
        int startIdx = (num - 1) * size;
        int endIdx = num * size;
        endIdx = endIdx >= articleVoList.size() ? articleVoList.size() - 1 : endIdx;
        articleVoList = articleVoList.subList(startIdx, endIdx);
        Page<ArticleVo> pageVo = new PageImpl(articleVoList, pageable, articles.size());
        return pageVo;
    }

    @Override
    public ArticleVo detailArticle(Integer id) {
        Article article = articleMapper.selectById(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtil.copyProperties(article, articleVo);
        return articleVo;
    }

    @Autowired
    private DocProperty docProperty;

    @Override
    public void download(HttpServletResponse response) throws IOException {
        // 下载本地文件
        File file = new File(docProperty.getAccurateDir());
        File parentFile = file.getParentFile();

        String zipfileName = parentFile.getName() + compress_suffix;
        String zipPath = parentFile.getParent() + File.separator + zipfileName;
        File zipFile = null;

        zipFile = new File(zipPath);
        if (zipFile.exists()) {
            boolean delete = zipFile.delete();
            logger.info("删除是否成功：" + delete);
        }

        ZipUtil.zip(parentFile.getPath(), zipPath);
        // 读到流中
        InputStream inStream = new FileInputStream(zipPath);// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.addHeader("Content-Disposition", "attachment; filename=\"" + zipfileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inStream.close();
        }
    }

    /**
     * 递归查询子节点
     *
     * @param rootMenu    根节点
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
                            if (menu.getName().equals(article.getName())) {
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

    private List<ArticleTreeVo> getArticleList(Integer menuId, List<ArticleTreeVo> articleList) {
        List<ArticleTreeVo> collect = articleList.stream().filter(item -> item.getDirectoryId().equals(menuId)).collect(Collectors.toList());

        return collect;
    }

}
