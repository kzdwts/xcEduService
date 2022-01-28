package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;

/**
 * 学习工程异常枚举
 *
 * @author Kang Yong
 * @date 2022/1/28
 * @since 1.0.0
 */
@ToString
public enum LearningCode implements ResultCode {

    LEARNING_GETMEDIA_ERROR(false, 10000, "获取视频播放地址出错"),

    ;
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String msg;

    private LearningCode(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
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
    public String msg() {
        return msg;
    }
}
