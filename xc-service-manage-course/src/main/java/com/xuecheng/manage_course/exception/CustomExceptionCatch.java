package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**课程管理自定义异常，其中定义异常类型及错误代码，提示信息
 * @author: MuYaHai
 * Date: 2019/12/17, Time: 21:25
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        //除了CustomerException以外的异常类型及对应错误代码的定义，如果不定义则统一返回错误信息
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
