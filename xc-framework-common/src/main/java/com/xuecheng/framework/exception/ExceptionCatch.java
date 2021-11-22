package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import jdk.nashorn.internal.objects.NativeUint8Array;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获类
 */
@Slf4j
@ControllerAdvice // 控制器增强
public class ExceptionCatch {

    // 不可变，且线程安全的map
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTION_MAP;

    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    static {
        // 非法参数异常
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALIDATE_PARAM);
    }

    /**
     * 捕获CustomerException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CustomerException.class)
    @ResponseBody
    public ResponseResult customerException(CustomerException e) {
        // 打印错误信息
        log.error("catch exception: {}", e.getMessage());
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    /**
     * 捕获Exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e) {
        // 打印错误信息
        log.error("catch exception: {}", e.getMessage());

        if (EXCEPTION_MAP == null) {
            EXCEPTION_MAP = builder.build();
        }

        ResultCode resultCode = EXCEPTION_MAP.get(e.getClass());
        if (!ObjectUtils.isEmpty(resultCode)) {
            return new ResponseResult(resultCode);
        }

        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

//    不这样捕获了，换一种方式
//    /**
//     * 非法参数异常捕获
//     *
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    @ResponseBody
//    public ResponseResult httpMessageNotReadableException(HttpMessageNotReadableException e) {
//        // 打印错误信息
//        log.error("catch exception: {}", e.getMessage());
//        return new ResponseResult(CommonCode.INVALIDATE_PARAM);
//    }
}
