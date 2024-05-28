package com.better.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.better.bean.Directory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * 目录mapper
 *
 * @waring NULL
 * @return
 * @author SL
 * @date 2022/7/11 4:28 PM
 */
@Repository
public interface DirectoryMapper extends BaseMapper<Directory> {

    /**
     * 根据版本信息获取对应的目录结构(根目录)
     *
     * @param parentId
     * @return java.util.List<com.asiainfo.ahweb.common.entity.Directory>
     * @waring NULL
     * @author SL
     * @date 2022/7/16 9:05 PM
     */
    List<Directory> selectDirectoryByParentId(@Param("parentId") Integer parentId);

    /**
     * 查询所有目录
     *
     * @param
     * @waring NULL
     * @return java.util.List<com.asiainfo.ahweb.common.entity.Directory>
     * @author SL
     * @date 2022/7/26 9:05 PM
     */
    List<Directory> selectAll();

    List<Directory> getAllByName(@Param("name") String name);

    List<Directory> getAllByNameAndParentId(@Param("name") String name, @Param("parentId") Integer parentId);
    List<Directory> getAl(@Param("name") String name, @Param("parentId") Integer parentId);

    int insertBatch(@Param("directoryCollection") Collection<Directory> directoryCollection);
}
