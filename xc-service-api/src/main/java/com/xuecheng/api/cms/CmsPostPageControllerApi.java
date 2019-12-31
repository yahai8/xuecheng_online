package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;

/**
 * @author: MuYaHai
 * Date: 2019/11/28, Time: 19:22
 */
@Api(value = "页面发布管理接口")
public interface CmsPostPageControllerApi {

    @ApiOperation("发布页面")
    public ResponseResult postPage(String pageId);

    @ApiOperation("一键发布")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
