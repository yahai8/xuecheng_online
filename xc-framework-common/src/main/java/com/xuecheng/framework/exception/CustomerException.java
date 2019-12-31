package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @author: MuYaHai
 * Date: 2019/11/23, Time: 22:34
 */
//继承runtimeexception不需要声明抛出的异常类型，或者捕捉异常
public class CustomerException extends RuntimeException {
    private ResultCode resultCode;

    public CustomerException(ResultCode resultCode) {
        //异常信息为错误代码加错误信息
        super("错误代码："+resultCode.code()+",异常信息："+resultCode.message());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
