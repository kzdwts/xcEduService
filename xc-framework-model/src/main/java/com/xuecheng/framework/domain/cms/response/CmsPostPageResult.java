package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 一键发布返回信息
 *
 * @author Kang Yong
 * @date 2021/12/27
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult {

    /**
     * 页面url
     */
    private String pageUrl;

    public CmsPostPageResult(ResultCode resultCode, String pageUrl) {
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}
