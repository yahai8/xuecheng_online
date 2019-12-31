package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author: MuYaHai
 * Date: 2019/12/12, Time: 17:47
 */
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {
    List<TeachplanMedia> findByCourseId(String courseId);
}
