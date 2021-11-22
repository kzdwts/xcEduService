package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 业务异常类
 */
public class CustomerException extends RuntimeException {

    private ResultCode resultCode;

    public CustomerException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public CustomerException() {
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
