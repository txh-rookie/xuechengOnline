package com.kevintam.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kevintam.auth.mapper.XcUserMapper;
import com.kevintam.auth.model.dto.AuthParamsDto;
import com.kevintam.auth.model.dto.XcUserExt;
import com.kevintam.auth.model.po.XcUser;
import com.kevintam.auth.service.AuthService;
import com.kevintam.study.exception.StudyException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/10
 */
@Service("password_authService")
public class PasswordAuthService implements AuthService {

    @Autowired
    private XcUserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 定义的是账号密码登录
     * @param authParamsDto 统一请求的类型
     * @return
     */
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        String username = authParamsDto.getUsername();
        XcUser xcUser = userMapper.selectOne(new QueryWrapper<XcUser>().eq("username", username));
        if(xcUser==null){
            throw new StudyException("该用户存在");
        }
        //校验我们的密码
        String passwordForm = authParamsDto.getPassword();
        String passwordDB = xcUser.getPassword();
        boolean matches = passwordEncoder.matches(passwordForm, passwordDB);
        if(!matches){
            throw new StudyException("账号或密码错误");
        }

        XcUserExt userExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,userExt);
        //拿到密码给框架。security会帮我们进行校验
        return userExt;
    }
}
