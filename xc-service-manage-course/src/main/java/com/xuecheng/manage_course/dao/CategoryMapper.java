package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 15:16
 */
@Mapper
public interface CategoryMapper {
    public CategoryNode findCategoryList();
}
