package com.geek.oauth.wx.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.geek.auth.common.domain.OauthUser;
import com.geek.auth.common.service.IOauthUserService;
import com.geek.common.constant.Constants;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.core.domain.model.LoginUser;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.MessageUtils;
import com.geek.common.utils.SecurityUtils;
import com.geek.common.utils.StringUtils;
import com.geek.framework.manager.AsyncManager;
import com.geek.framework.manager.factory.AsyncFactory;
import com.geek.framework.web.service.SysLoginService;
import com.geek.framework.web.service.TokenService;
import com.geek.framework.web.service.UserDetailsServiceImpl;
import com.geek.oauth.wx.constant.WxPubConstant;
import com.geek.oauth.wx.service.WxLoginService;
import com.geek.system.service.ISysUserService;

@Service
public class WxPubLoginServiceImpl implements WxLoginService {

    @Autowired
    private WxPubConstant wxH5Constant;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IOauthUserService oauthUserService;

    @Autowired
    private SysLoginService sysLoginService;

    @Override
    public String doLogin(String code, boolean autoRegister) {
        JsonNode doAuth = doAuth(
                wxH5Constant.getUrl(),
                wxH5Constant.getAppId(),
                wxH5Constant.getAppSecret(),
                code);
        String openid = doAuth.get("openid").asText();
        OauthUser selectOauthUser = oauthUserService.selectOauthUserByUUID(openid);
        SysUser sysUser = null;
        if (selectOauthUser == null) {
            if (autoRegister) {
                sysUser = new SysUser();
                sysUser.setUserName(openid);
                sysUser.setNickName(openid);
                sysUser.setPassword(SecurityUtils.encryptPassword(code));
                userService.registerUser(sysUser);
                OauthUser oauthUser = new OauthUser();
                oauthUser.setUserId(sysUser.getUserId());
                oauthUser.setOpenId(doAuth.get("openid").asText());
                oauthUser.setUuid(doAuth.get("openid").asText());
                oauthUser.setSource("WXMiniApp");
                oauthUser.setAccessToken(doAuth.get("session_key").asText());
                oauthUserService.insertOauthUser(oauthUser);
            }
        } else {
            sysUser = userService.selectUserById(selectOauthUser.getUserId());
        }
        if (sysUser == null) {
            throw new ServiceException("该微信未绑定用户");
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(sysUser.getUserName(), Constants.LOGIN_SUCCESS,
                MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) userDetailsServiceImpl.createLoginUser(sysUser);
        sysLoginService.recordLoginInfo(loginUser.getUserId());
        return tokenService.createToken(loginUser);
    }

    @Override
    public String doRegister(OauthUser oauthUser) {
        if (StringUtils.isEmpty(oauthUser.getCode())) {
            return "没有凭证";
        }
        if (oauthUser.getUserId() == null) {
            return "请先注册账号";
        }
        JsonNode doAuth = doAuth(
                wxH5Constant.getUrl(),
                wxH5Constant.getAppId(),
                wxH5Constant.getAppSecret(),
                oauthUser.getCode());
        oauthUser.setOpenId(doAuth.get("openid").asText());
        oauthUser.setUuid(doAuth.get("openid").asText());
        oauthUser.setSource("WXPub");
        oauthUser.setAccessToken(doAuth.get("session_key").asText());
        oauthUserService.insertOauthUser(oauthUser);
        return "";
    }

}
