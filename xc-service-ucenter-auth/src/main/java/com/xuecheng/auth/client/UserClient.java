package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户中心接口
 *
 * @author Kang Yong
 * @date 2022/2/7
 * @since 1.0.0
 */
@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient {

    /**
     * 根据username查询用户信息
     *
     * @param username {@link String}
     * @return {@link XcUserExt}
     * @author Kang Yong
     * @date 2022/2/7
     */
    @GetMapping("/ucenter/getuserext")
    XcUserExt getUserext(@RequestParam("username") String username);

}
