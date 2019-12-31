package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: MuYaHai
 * Date: 2019/12/17, Time: 16:37
 */
@Api(value = "用户中心", description = "用户管理中心")
public interface UcenterControllerApi {

    @ApiOperation("根据用户名查询用户详细信息")
    public XcUserExt getXcUserExt(String name);
}
