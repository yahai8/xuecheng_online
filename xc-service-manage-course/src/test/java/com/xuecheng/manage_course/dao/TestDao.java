package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import io.swagger.annotations.ApiImplicitParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }
    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }

    @Test
    public void findAll() {
        TeachplanNode teachplanList = teachplanMapper.findTeachplanList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanList);
    }

    @Test
    public void listPage() {
        CourseListRequest courseListRequest = new CourseListRequest();
        PageHelper.startPage(1, 10);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        List<CourseInfo> result = courseListPage.getResult();
        long total = courseListPage.getTotal();
        System.out.println(total);
    }

    @Test
    public void category() {
        CategoryNode categoryList = categoryMapper.findCategoryList();
        System.out.println(categoryList);
    }

    @Test
    public void testRibbon() {
        String serviceid = "XC-SERVICE-MANAGE-COURSE";
        ResponseEntity<CourseBase> forEntity = restTemplate.getForEntity("http://" + serviceid + "/course/getCoursebaseById/40281f81640220d601640222665b0001", CourseBase.class);
        CourseBase body = forEntity.getBody();
        System.out.println(body);
    }
}
