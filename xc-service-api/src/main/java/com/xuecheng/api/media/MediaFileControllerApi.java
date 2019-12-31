package com.xuecheng.api.media;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: MuYaHai
 * Date: 2019/12/12, Time: 16:36
 */
@Api(value = "媒资管理", description = "对媒资信息进行增删改查")
public interface MediaFileControllerApi {

    @ApiOperation("查询文件列表")
    public QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);

    @ApiOperation("删除文件")
    public ResponseResult delMedia(String fileId);

    @ApiOperation("处理媒资文件")
    public ResponseResult process(String fileId);
}
