package com.kevintam.auth.utils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/10
 */
@Data
@Component
public class ConstantWxUtils implements InitializingBean {

    @Value("${wx.open.app_id}")
    private String appId;
    @Value("${wx.open.app_secret}")
    private String appSecret;
    @Value("${wx.open.redirect_url}")
    private String redirectUrl;

    public static String WX_APPID;
    public static String WX_APP_SECRET;
    public static String WX_APP_REDIRECT_URL;

    /**
     * 进行初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        WX_APPID=appId;
        WX_APP_SECRET=appSecret;
        WX_APP_REDIRECT_URL=redirectUrl;
    }
}
