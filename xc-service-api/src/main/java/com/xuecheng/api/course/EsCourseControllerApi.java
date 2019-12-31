package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author: MuYaHai
 * Date: 2019/12/9, Time: 15:26
 */
@Api(value = "课程搜索", description = "负责课程页面的课程搜索功能", tags = {"课程搜索"})
public interface EsCourseControllerApi {

    @ApiOperation("课程搜索")
    public QueryResponseResult<CoursePub> list(int page, int size,
                                               CourseSearchParam courseSearchParam) throws Exception;

    @ApiOperation("根据id查询课程信息")
    public Map<String, CoursePub> getall(String id);


    @ApiOperation("根据课程计划查询媒资信息")
    public TeachplanMediaPub getmedia(String teachplanId);
}
