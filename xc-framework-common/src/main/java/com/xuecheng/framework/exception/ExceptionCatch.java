package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: MuYaHai
 * Date: 2019/11/23, Time: 22:40
 */
@ControllerAdvice
public class ExceptionCatch {
    private static final Logger  LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //使用excepitons来存放异常类型和错误代码的映射，ImmutableMap的特点是一旦创建无法被改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    //捕获异常，用户自定义异常
    @ExceptionHandler(CustomerException.class)
    @ResponseBody
    public ResponseResult customerException(CustomerException e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(),e);
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode);
        return responseResult;
    }

    //捕获异常，捕获不可预知的异常HttpMessageNotReadableException
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(),e);
        //拦截异常之后判断EXCEPTIONS是否为空，为空就进行构建
        if (EXCEPTIONS==null){
            EXCEPTIONS=builder.build();
        }
        //如果EXCEPTIONS不为空，就根据异常类去查找错误代码
        final ResultCode resultCode =EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        //如果查出的resultCode不为空，就把错误代码传到前端去
        if (resultCode !=null){
            responseResult = new ResponseResult(resultCode);
        }else {
            //resultCode为空就统一处理代码为9999服务器错误
            responseResult= new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return responseResult;
    }

    //录入一些常见异常信息和错误代码
    static {
        builder.put(HttpMessageNotReadableException.class,CommonCode.SERVER_ERROR);
    }
}
