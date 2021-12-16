package com.xuecheng.file_system.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 文件管理实体类
 *
 * @author Kang Yong
 * @date 2021/12/16
 * @since 1.0.0
 */
public interface FileSystemRepository extends MongoRepository<FileSystem, String> {
}
