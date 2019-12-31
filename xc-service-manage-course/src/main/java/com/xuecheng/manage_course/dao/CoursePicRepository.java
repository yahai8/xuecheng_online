package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: MuYaHai
 * Date: 2019/12/2, Time: 20:38
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {
    long deleteByCourseid(String courseId);
}
