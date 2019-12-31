package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 10:08
 */
@Mapper
public interface TeachplanMapper {
    TeachplanNode findTeachplanList(String courseid);
}
