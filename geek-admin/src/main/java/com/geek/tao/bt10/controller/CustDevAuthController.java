package com.geek.tao.bt10.controller;

import com.geek.common.annotation.Anonymous;
import com.geek.common.constant.Constants;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.core.domain.model.LoginUser;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.StringUtils;
import com.geek.framework.web.service.TokenService;
import com.geek.framework.web.service.UserDetailsServiceImpl;
import com.geek.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 开发环境模拟登录（跳过微信授权），仅 profile=dev 可用。
 */
@RestController
@RequestMapping("/bt10/cust/auth")
public class CustDevAuthController extends BaseController {

    @Autowired
    private Environment environment;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private TokenService tokenService;

    public static class MockLoginReq {
        /** 默认 admin */
        public String username;
    }

    @Anonymous
    @PostMapping("/mock-login")
    public AjaxResult mockLogin(@RequestBody(required = false) MockLoginReq req) {
        if (!environment.acceptsProfiles(Profiles.of("dev"))) {
            throw new ServiceException("仅开发环境可用");
        }
        String username = (req == null || StringUtils.isEmpty(req.username)) ? "admin" : req.username.trim();
        SysUser user = userService.selectUserByUserName(username);
        if (user == null) {
            return error("用户不存在: " + username);
        }
        LoginUser loginUser = (LoginUser) userDetailsService.createLoginUser(user);
        String token = tokenService.createToken(loginUser);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        ajax.put("userId", String.valueOf(user.getUserId()));
        ajax.put("userName", user.getUserName());
        ajax.put("data", Map.of(
                "token", token,
                "userId", String.valueOf(user.getUserId()),
                "userName", user.getUserName()
        ));
        return ajax;
    }
}
