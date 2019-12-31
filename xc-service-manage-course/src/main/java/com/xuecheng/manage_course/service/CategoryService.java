package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 15:32
 */
@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    //查询课程分类
    public CategoryNode findCategoryList() {
        return categoryMapper.findCategoryList();
    }
}
