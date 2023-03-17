package com.kevintam.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevintam.auth.mapper.XcRoleMapper;
import com.kevintam.auth.mapper.XcUserMapper;
import com.kevintam.auth.mapper.XcUserRoleMapper;
import com.kevintam.auth.model.dto.AuthParamsDto;
import com.kevintam.auth.model.dto.XcUserExt;
import com.kevintam.auth.model.po.XcRole;
import com.kevintam.auth.model.po.XcUser;
import com.kevintam.auth.model.po.XcUserRole;
import com.kevintam.auth.service.AuthService;
import com.kevintam.auth.service.WxAuthService;
import com.kevintam.auth.utils.ConstantWxUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/10
 * 具体的策略类
 */
@Service("WX_authService")
public class WXAuthService implements AuthService, WxAuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private XcUserMapper userMapper;

    @Autowired
    private XcUserRoleMapper userRoleMapper;

    @Autowired
    private WXAuthService current;

    /**
     * 使用wx进行登录
     *
     * @param authParamsDto 统一请求的类型
     * @return
     */
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        String username = authParamsDto.getUsername();
        XcUser user = userMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        XcUserExt userExt = new XcUserExt();
        BeanUtils.copyProperties(user,userExt);
        return userExt;
    }

    /**
     * 根据code去获取用户信息,然后将用户信息保存在我们的数据库中
     *
     * @param code
     * @return
     */
    @Override
    public XcUser wxAuth(String code) {
        Map<String, String> resultToken = getToken(code);
        String access_token = resultToken.get("access_token");
        String openid = resultToken.get("openid");
        //拿到了token
        Map<String, String> userInfos = getUserInfos(access_token,openid);
        //将用户信息保存到数据库中
        XcUser xcUser = current.addWxUser(userInfos);
        return xcUser;
    }

    /**
     * 将我们的用户保存到数据库中
     *
     * @param userInfos {
     *                  "openid":"OPENID",
     *                  "nickname":"NICKNAME",
     *                  "sex":1,
     *                  "province":"PROVINCE",
     *                  "city":"CITY",
     *                  "country":"COUNTRY",
     *                  "headimgurl": "https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
     *                  "privilege":[
     *                  "PRIVILEGE1",
     *                  "PRIVILEGE2"
     *                  ],
     *                  "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
     *                  }
     */
    @Transactional
    public XcUser addWxUser(Map<String, String> userInfos) {
        String nickname = userInfos.get("nickname");
        String unionid = userInfos.get("unionid");//全局为一的标志
        String pic = userInfos.get("headimgurl");
        //去数据库中进行查询
        XcUser user = userMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getWxUnionid, unionid));
        if (user != null) {//用户信息有值，表示登录成功，等于null，则表示用户登录失败
            return user;
        }
        //保存用户信息
        XcUser xcUser = new XcUser();
        //保存id信息
        String userId = UUID.randomUUID().toString();
        xcUser.setId(userId);
        xcUser.setUsername(unionid);
        xcUser.setPassword(unionid);
        xcUser.setNickname(nickname);
        xcUser.setName(nickname);
        xcUser.setWxUnionid(unionid);
        xcUser.setStatus("1");
        xcUser.setUserpic(pic);
        xcUser.setStatus("17");//用户一个状态,17表示是学生
        xcUser.setCreateTime(LocalDateTime.now());//当前时间
        int insert = userMapper.insert(xcUser);
        //保存用户角色
        XcUserRole xcUserRole = new XcUserRole();
        xcUserRole.setId(UUID.randomUUID().toString());
        xcUserRole.setUserId(userId);
        xcUserRole.setRoleId("17");//是学生角色
        xcUserRole.setCreateTime(LocalDateTime.now());
        userRoleMapper.insert(xcUserRole);
        return xcUser;
    }

    /**
     * 根据我们的token去获取用户信息
     * @return
     */
    private Map<String, String> getUserInfos(String access_token,String openid) {
//        https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        String url_template = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        String url = String.format(url_template, access_token, openid);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        //解决乱码问题
        String result = new String(responseEntity.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> resultUserInfo = null;
        try {
            resultUserInfo = objectMapper.readValue(result, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultUserInfo;
    }

    /**
     * 根据code去获取我们的token
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     *
     * @param code
     * @return
     */
    public Map<String, String> getToken(String code) {
        String url_template = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        //获取到填充完的url
        String url = String.format(url_template, ConstantWxUtils.WX_APPID, ConstantWxUtils.WX_APP_SECRET, code);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        String body = exchange.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> resultTokens = null;
        try {
            resultTokens = objectMapper.readValue(body, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultTokens;
    }
}
