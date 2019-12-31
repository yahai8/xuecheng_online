package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: MuYaHai
 * Date: 2019/11/23, Time: 19:34
 */
@Api(value = "cms模板管理接口", description = "cms模板管理接口,提供模板增删改查")
public interface CmsTemplateControllerApi {

    @ApiOperation("查询所有模板")
    public QueryResponseResult findList();
}
