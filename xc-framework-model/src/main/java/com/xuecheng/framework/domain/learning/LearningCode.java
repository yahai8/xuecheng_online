package com.xuecheng.framework.domain.learning;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 18:33.
 * @Modified By:
 */

@ToString
public enum LearningCode implements ResultCode{
    LEARNING_GETMEDIA_ERROR(false,32001,"获取媒资信息出错"),
    CHOOSECOURSE_USERISNULL(false,32002,"选课用户为空"),
    CHOOSECOURSE_TASKISNULL(false,32003,"选课任务为空");
//    private static ImmutableMap<Integer, CommonCode> codes ;
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private LearningCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
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
