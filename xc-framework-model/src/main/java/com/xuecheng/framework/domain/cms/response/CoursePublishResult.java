package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程预览
 *
 * @author Kang Yong
 * @date 2021/12/27
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {

    private String previewUrl;

    public CoursePublishResult(ResultCode resultCode, String previewUrl) {
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
