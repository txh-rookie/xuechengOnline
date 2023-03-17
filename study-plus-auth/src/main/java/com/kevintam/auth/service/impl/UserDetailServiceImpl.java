package com.kevintam.auth.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevintam.auth.mapper.XcMenuMapper;
import com.kevintam.auth.mapper.XcRoleMapper;
import com.kevintam.auth.mapper.XcUserMapper;
import com.kevintam.auth.mapper.XcUserRoleMapper;
import com.kevintam.auth.model.dto.AuthParamsDto;
import com.kevintam.auth.model.dto.XcUserExt;
import com.kevintam.auth.model.po.XcMenu;
import com.kevintam.auth.service.AuthService;
import com.kevintam.study.exception.StudyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/9
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private XcUserMapper userMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private XcMenuMapper menuMapper;


    /**
     * 根据我们的username去查询我们用户信息，然后将用户信息，放到我们应用上下文
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new StudyException("用户名不能为null");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        AuthParamsDto authParamsDto = null;
        try {
            authParamsDto = objectMapper.readValue(username, AuthParamsDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (authParamsDto == null) {
            throw new StudyException("参数不合法");
        }

        if (authParamsDto.getUsername().length() < 5) {
            throw new StudyException("用户名长度不能小与5");
        }
        if (authParamsDto.getAuthType() == null) {
            throw new StudyException("该参数不能为null");
        }
        //根据我们的请求的类型不同可以有不同的参数
        String authType = authParamsDto.getAuthType();
        AuthService authService = applicationContext.getBean(authType + "_authService", AuthService.class);
        //完成认证
        XcUserExt execute = authService.execute(authParamsDto);
        //封装用户信息
        return getUser(execute);
    }

    //进行封装
    public UserDetails getUser(XcUserExt userExt) {
        ObjectMapper objectMapper = new ObjectMapper();
        //拿到用户相关的权限
        List<XcMenu> menus = menuMapper.selectPermissionByUserId(userExt.getId());
        List<String> menuList = menus.stream().map(XcMenu::getCode).collect(Collectors.toList());
        //转换成字符数组
        String[] authorizations = menuList.toArray(new String[0]);
        String password = userExt.getPassword();
        //将我们的用户密码置空
        userExt.setPassword(null);
        String user = null;
        try {
            user = objectMapper.writeValueAsString(userExt);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
       return User.withUsername(user).password(password).authorities(authorizations).build();
    }
}
