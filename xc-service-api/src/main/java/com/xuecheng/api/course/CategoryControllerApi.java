package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 15:32
 */
@Api(value = "课程分类接口")
public interface CategoryControllerApi {

    @ApiOperation("查询课程分类")
    public CategoryNode findCategoryList();
}
