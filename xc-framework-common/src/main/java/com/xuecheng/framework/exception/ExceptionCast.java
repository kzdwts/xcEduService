package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 异常抛出
 */
public class ExceptionCast {

    /**
     * 统一异常抛出类
     *
     * @param resultCode
     */
    public static void cast(ResultCode resultCode) {
        throw new CustomerException(resultCode);
    }

}
