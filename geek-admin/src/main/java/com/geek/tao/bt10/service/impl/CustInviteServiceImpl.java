package com.geek.tao.bt10.service.impl;

import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.StringUtils;
import com.geek.tao.bt10.domain.Identities;
import com.geek.tao.bt10.domain.UserIdentities;
import com.geek.tao.bt10.domain.UserInvite;
import com.geek.tao.bt10.domain.UserPointLogs;
import com.geek.tao.bt10.mapper.IdentitiesMapper;
import com.geek.tao.bt10.mapper.UserIdentitiesMapper;
import com.geek.tao.bt10.mapper.UserInviteMapper;
import com.geek.tao.bt10.mapper.UserPointLogsMapper;
import com.geek.tao.bt10.service.ICustInviteService;
import com.geek.system.service.ISysUserService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustInviteServiceImpl implements ICustInviteService {

    public static final String IDENTITY_P6 = "IDENTITY_P6";

    @Autowired
    private UserInviteMapper userInviteMapper;
    @Autowired
    private UserIdentitiesMapper userIdentitiesMapper;
    @Autowired
    private IdentitiesMapper identitiesMapper;
    @Autowired
    private UserPointLogsMapper userPointLogsMapper;
    @Autowired
    private ISysUserService userService;

    @Override
    @Transactional
    public Map<String, Object> bind(Long inviteeUserId, Long inviterId, String identityCode) {
        if (inviteeUserId == null) throw new ServiceException("请先登录");
        if (inviterId == null) throw new ServiceException("inviterId 不能为空");
        if (inviteeUserId.equals(inviterId)) throw new ServiceException("不能邀请自己");

        String code = StringUtils.isEmpty(identityCode) ? IDENTITY_P6 : identityCode.trim();

        UserInvite exists = userInviteMapper.selectOneByQuery(QueryWrapper.create()
                .from(UserInvite.class)
                .eq(UserInvite::getUserId, inviteeUserId)
                .eq(UserInvite::getIdentityCode, code));
        if (exists != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("bound", true);
            data.put("rewarded", Boolean.TRUE.equals(exists.getRewardClaimed()));
            data.put("inviteId", String.valueOf(exists.getId()));
            return data;
        }

        SysUser invitee = userService.selectUserById(inviteeUserId);
        if (invitee == null) throw new ServiceException("用户不存在");
        // 仅新用户：账号创建时间在 24h 内
        Date createTime = invitee.getCreateTime();
        if (createTime != null) {
            Instant created = createTime.toInstant();
            if (created.isBefore(Instant.now().minus(24, ChronoUnit.HOURS))) {
                throw new ServiceException("仅新用户可绑定邀请");
            }
        }

        SysUser inviter = userService.selectUserById(inviterId);
        if (inviter == null) throw new ServiceException("邀请人不存在");

        UserInvite row = new UserInvite();
        row.setUserId(inviteeUserId);
        row.setInviterId(inviterId);
        row.setInviteCode(String.valueOf(inviterId));
        row.setInviteTime(Instant.now());
        row.setIdentityCode(code);
        row.setRewardStatus("REWARDED");
        row.setRewardClaimed(true);
        row.setRewardPoints(1);
        userInviteMapper.insert(row);

        UserIdentities ui = ensureIdentity(inviterId, code);
        int before = ui.getAvailablePoints() == null ? 0 : ui.getAvailablePoints();
        int after = before + 1;
        ui.setAvailablePoints(after);
        userIdentitiesMapper.update(ui);

        UserPointLogs log = new UserPointLogs();
        log.setUserId(inviterId);
        log.setActionType("INVITE_REWARD");
        log.setPoints(1L);
        log.setBalanceBefore((long) before);
        log.setBalanceAfter((long) after);
        log.setRelatedType("INVITE");
        log.setRelatedId(row.getId());
        log.setIdentityCode(code);
        userPointLogsMapper.insert(log);

        Map<String, Object> data = new HashMap<>();
        data.put("bound", true);
        data.put("rewarded", true);
        data.put("inviteId", String.valueOf(row.getId()));
        data.put("inviterAvailablePoints", after);
        return data;
    }

    private UserIdentities ensureIdentity(Long userId, String identityCode) {
        UserIdentities ui = userIdentitiesMapper.selectOneByQuery(QueryWrapper.create()
                .from(UserIdentities.class)
                .eq(UserIdentities::getUserId, userId)
                .eq(UserIdentities::getIdentityCode, identityCode));
        if (ui != null) {
            if (ui.getAvailablePoints() == null) {
                ui.setAvailablePoints(0);
                userIdentitiesMapper.update(ui);
            }
            return ui;
        }
        Identities def = identitiesMapper.selectOneById(identityCode);
        int defaults = def != null && def.getDefaultAvailablePoints() != null
                ? def.getDefaultAvailablePoints() : 3;
        ui = new UserIdentities();
        ui.setUserId(userId);
        ui.setIdentityCode(identityCode);
        ui.setIsPrimary(false);
        ui.setBizStatus("ACTIVE");
        ui.setAcquiredTime(Instant.now());
        ui.setAvailablePoints(defaults);
        ui.setSourceType("INVITE_ENSURE");
        userIdentitiesMapper.insert(ui);
        return ui;
    }
}
