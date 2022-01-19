package com.xuecheng.manage_media.dao;

import com.xuecheng.framework.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 媒资文件管理 dao
 *
 * @author Kang Yong
 * @date 2022/1/16
 * @since 1.0.0
 */
public interface MediaFileRepository extends MongoRepository<MediaFile, String> {
}
