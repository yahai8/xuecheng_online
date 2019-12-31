package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: MuYaHai
 * Date: 2019/11/25, Time: 20:30
 */
@Api(value = "cms配置管理接口", description = "cms配置管理接口，负责数据模型的管理、查询接口")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询cms配置信息")
    public CmsConfig getModel(String id);
}
