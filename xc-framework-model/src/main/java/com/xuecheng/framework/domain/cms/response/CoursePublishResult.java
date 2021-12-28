package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 课程发布响应结果
 *
 * @author Kang Yong
 * @date 2021/12/21
 * @since 1.0.0
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {

    private String previewUrl;

    public CoursePublishResult(ResultCode resultCode, String previewUrl) {
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
