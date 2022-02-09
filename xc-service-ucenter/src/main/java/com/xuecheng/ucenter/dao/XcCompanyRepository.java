package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompany;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 企业
 *
 * @author Kang Yong
 * @date 2022/2/9
 * @since 1.0.0
 */
public interface XcCompanyRepository extends JpaRepository<XcCompany, String> {
}
