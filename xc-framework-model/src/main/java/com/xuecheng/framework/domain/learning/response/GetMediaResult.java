package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 媒资系统响应参数
 *
 * @author Kang Yong
 * @date 2022/1/28
 * @since 1.0.0
 */
@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {

    /**
     * 媒资文件播放地址
     */
    private String fileUrl;

    public GetMediaResult(ResultCode resultCode, String fileUrl) {
        super(resultCode);
        this.fileUrl = fileUrl;
    }
}
