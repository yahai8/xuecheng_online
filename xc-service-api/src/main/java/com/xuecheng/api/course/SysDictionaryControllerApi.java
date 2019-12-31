package com.xuecheng.api.course;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 15:46
 */
@Api(value = "字典查询接口", description = "提供课程等级，学习模式的查询")
public interface SysDictionaryControllerApi {

    @ApiOperation("查询课程等级,学习模式")
    public SysDictionary getDictionary(String type);
}
