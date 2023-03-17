package com.kevintam.auth.controller;

import com.kevintam.auth.model.po.XcUser;
import com.kevintam.auth.service.WxAuthService;
import com.kevintam.auth.utils.ConstantWxUtils;
import com.kevintam.study.exception.StudyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/10
 */
@Controller
public class WxLoginController {

    @Autowired
    private WxAuthService wxAuthService;

    /**
     * 去向我们的微信平台拿到我们的微信登录二维码,用户登录授权之后，会跳转到我们的callback
     * @return
     */
    @GetMapping("/login")
    public String login() {
        //获取微信的code
        //微信的地址
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String redirectUrl = ConstantWxUtils.WX_APP_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(baseUrl, ConstantWxUtils.WX_APPID, redirectUrl, "atguigu");
        //1、请求wx的地址，通过重定向
        return "redirect:" + url;
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code){
      //用户授权之后，我们会拿到一个授权吗
       if(code==null){
           throw new StudyException("code值不能为null");
       }
//        https://api.weixin.qq.com/sns/oauth2/access_token
//        ?appid=APPID&secret=SECRET&code=CODE
//        &grant_type=authorization_code
        //模版url
        XcUser xcUser = wxAuthService.wxAuth(code);
       //如果为null，表示失败,就进行重定向
        if(xcUser==null){
            return "redirect:http://www.xuecheng-plus.com/error.html";
        }
        String username = xcUser.getUsername();
        return "redirect:http://www.xuecheng-plus.com/sign.html?username="+username+"&authType=wx";
    }

}
