package com.xuecheng.manage.cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;

/**
 * 字典管理
 *
 * @author Kang Yong
 * @date 2021/12/13
 * @since 1.0.0
 */
public interface SysDictionaryService {

    /**
     * 根据字典类型查询字典数据
     *
     * @param type
     * @return
     */
    SysDictionary findDictionaryByType(String type);
}
