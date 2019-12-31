package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @author: MuYaHai
 * Date: 2019/11/23, Time: 22:38
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode) {
        throw new CustomerException(resultCode);
    }
}
