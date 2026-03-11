package com.geek.web.controller.system;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.constant.Constants;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.domain.entity.SysMenu;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.core.domain.model.LoginBody;
import com.geek.common.core.domain.model.LoginUser;
import com.geek.common.core.text.Convert;
import com.geek.common.utils.DateUtils;
import com.geek.common.utils.Sb;
import com.geek.common.utils.SecurityUtils;
import com.geek.common.utils.StringUtils;
import com.geek.framework.web.service.SysLoginService;
import com.geek.framework.web.service.SysPermissionService;
import com.geek.framework.web.service.TokenService;
import com.geek.system.service.ISysConfigService;
import com.geek.system.service.ISysMenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 登录验证
 * 
 * @author geek
 */
@Tag(name = "登录验证")
@RestController
public class SysLoginController {

    @Resource
    private SysLoginService loginService;

    @Resource
    private ISysMenuService menuService;

    @Resource
    private SysPermissionService permissionService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ISysConfigService configService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @Operation(summary = "登录方法")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(
                loginBody.getUsername(),
                loginBody.getPassword(),
                loginBody.getCaptcha());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        if (StringUtils.isNotEmpty(user.getAvatar()) && !user.getAvatar().startsWith("http")) {
            try {
                user.setAvatar(Sb.getURL(user.getAvatar()));
            } catch (Exception ignored) {
            }
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        ajax.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
        ajax.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
        return ajax;
    }

    // 检查初始密码是否提醒修改
    public boolean initPasswordIsModify(Instant pwdUpdateInstant) {
        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateInstant == null;
    }

    // 检查密码是否过期
    public boolean passwordIsExpiration(Instant pwdUpdateInstant) {
        Integer passwordValidateDays = Convert
                .toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
        if (passwordValidateDays != null && passwordValidateDays > 0) {
            if (StringUtils.isNull(pwdUpdateInstant)) {
                // 如果从未修改过初始密码，直接提醒过期
                return true;
            }
            return DateUtils.differentDaysByMillisecond(DateUtils.getNowInstant(), pwdUpdateInstant) > passwordValidateDays;
        }
        return false;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @Operation(summary = "获取路由信息")
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
