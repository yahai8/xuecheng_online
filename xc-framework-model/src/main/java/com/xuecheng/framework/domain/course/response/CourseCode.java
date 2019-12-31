package com.xuecheng.framework.domain.course.response;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by admin on 2018/3/5.
 */
@ToString
public enum CourseCode implements ResultCode {
    COURSE_DENIED_DELETE(false,31001,"删除课程失败，只允许删除本机构的课程！"),
    COURSE_PUBLISH_PERVIEWISNULL(false,31002,"还没有进行课程预览！"),
    COURSE_PUBLISH_CDETAILERROR(false,31003,"创建课程详情页面出错！"),
    COURSE_PUBLISH_COURSEIDISNULL(false,31004,"课程Id为空！"),
    COURSE_PUBLISH_VIEWERROR(false,31005,"发布课程视图出错！"),
    COURSE_MEDIS_URLISNULL(false,31101,"选择的媒资文件访问地址为空！"),
    COURSE_ISNULL(false, 31006, "没有此课程"),
    COURSE_MARKET_ISNULL(false, 31007, "没有查询到课程营销"),
    COURSE_MEDIS_NAMEISNULL(false,31102,"选择的媒资文件名称为空！"),
    COURSE_PIC_ISNOTEXISIT(false,31103,"没有此图片，请刷新重试"),
    COURSE_BASE_ISNULL(false,31104,"课程基础信息为空"),
    COURSE_PUBLISH_INDEX_ERROR(false, 31105, "创建课程索引信息失败"),
    COURSE_MEDIA_TEACHPLAN_ISNULL(false,31106,"课程资源为空"),
    COURSE_MEDIA_TEACHPLAN_GRADEERROR(false,31107,"媒资课程等级错误");

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;
    private CourseCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, CourseCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, CourseCode> builder = ImmutableMap.builder();
        for (CourseCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
