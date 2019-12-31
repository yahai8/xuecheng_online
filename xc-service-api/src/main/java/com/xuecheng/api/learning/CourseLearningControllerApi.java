package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: MuYaHai
 * Date: 2019/12/13, Time: 20:38
 */

@Api(value = "录播课程学习管理", description = "录播课程学习管理")
public interface CourseLearningControllerApi {
    @ApiOperation("获取课程学习地址")
    public GetMediaResult getmedia(String courseId, String teachplanId);
}
