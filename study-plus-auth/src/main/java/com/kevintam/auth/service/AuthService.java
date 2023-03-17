package com.kevintam.auth.service;

import com.kevintam.auth.model.dto.AuthParamsDto;
import com.kevintam.auth.model.dto.XcUserExt;

/**
 * 抽象策略类
 */
public interface AuthService {
    /**
     * 认证方式
     * @param authParamsDto 统一请求的类型
     * @return
     */
    public XcUserExt execute(AuthParamsDto authParamsDto);
}
