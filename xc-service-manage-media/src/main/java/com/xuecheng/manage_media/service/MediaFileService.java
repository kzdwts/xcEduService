package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * 媒资文件管理
 *
 * @author Kang Yong
 * @date 2022/1/23
 * @since 1.0.0
 */
public interface MediaFileService {

    /**
     * 查询文件列表
     *
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return
     */
    QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);
}
