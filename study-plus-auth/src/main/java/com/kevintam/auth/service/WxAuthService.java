package com.kevintam.auth.service;

import com.kevintam.auth.model.po.XcUser;

public interface WxAuthService {
   public XcUser wxAuth(String code);
}
