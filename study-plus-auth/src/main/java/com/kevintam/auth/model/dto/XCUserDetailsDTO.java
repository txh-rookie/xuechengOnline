package com.kevintam.auth.model.dto;

import com.kevintam.auth.model.po.XcUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/9
 */
@Data
public class XCUserDetailsDTO extends XcUserExt implements UserDetails {

    /**
     * 获取用户的权限信息
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return null;
    }

    /**
     * 获取密码
     * @return
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 获取账号
     * @return
     */
    @Override
    public String getUsername() {
        return getName();
    }

    /**
     * 账户是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * 账户是否锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /*
    凭据未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 启用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}
