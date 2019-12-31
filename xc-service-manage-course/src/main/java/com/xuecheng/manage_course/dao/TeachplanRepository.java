package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 11:27
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {
    //根据课程id和父节点id查询出节点列表，可用此方法查询出根节点
    public List<Teachplan> findByCourseidAndParentid(String courseid, String parentid);
}
