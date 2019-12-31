package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: MuYaHai
 * Date: 2019/11/23, Time: 18:27
 */
@Api(value = "cms门户管理接口",description="cms门户管理接口，提供门户的增删改查")
public interface CmsSiteControllerApi{
    @ApiOperation("查询所有门户")
    public QueryResponseResult findList();
}
