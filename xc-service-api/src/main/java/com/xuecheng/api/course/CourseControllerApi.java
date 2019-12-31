package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CoursePublishResult;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author: MuYaHai
 * Date: 2019/11/28, Time: 21:06
 */
@Api(value = "课程管理接口", description = "课程管理接口，负责课程的增删改查")
public interface CourseControllerApi {

//    @ApiOperation("查询所有节点，用于显示")

    @ApiOperation("课程计划查询")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "courseid", value = "课程id", required = true, paramType = "path", defaultValue = "string")
    )
    public TeachplanNode findTeacherList(String courseid);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("分页查询课程")
    public QueryResponseResult findCourseListPage(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程")
    public ResponseResult add(CourseBase courseBase);

    @ApiOperation("修改课程")
    public ResponseResult updateCoursebase(String id, CourseBase courseBase);

    @ApiOperation("根据课程id查询课程信息")
    public CourseBase getCoursebaseById(String id);

    @ApiOperation("查询课程营销")
    public CourseMarket getCourseMarketById(String id);

    @ApiOperation("更新课程营销信息")
    public ResponseResult updateCourseMarket(String courseid, CourseMarket courseMarket);

    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("获取课程图片")
    public CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public ResponseResult delCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    public CourseView courseView(String id);

    @ApiOperation("预览课程")
    public CoursePublishResult preview(String id);

    @ApiOperation("课程发布")
    public CoursePublishResult publish(String id);

    @ApiOperation("保存媒资信息")
    public ResponseResult saveMedia(TeachplanMedia teachplanMedia);
}
