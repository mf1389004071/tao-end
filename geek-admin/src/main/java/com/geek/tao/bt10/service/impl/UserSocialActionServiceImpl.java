package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserSocialAction;
import com.geek.tao.bt10.mapper.UserSocialActionMapper;
import com.geek.tao.bt10.service.IUserSocialActionService;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户主页社交动作 服务实现
 */
@Service
public class UserSocialActionServiceImpl extends ServiceImpl<UserSocialActionMapper, UserSocialAction> implements IUserSocialActionService {

    private QueryChain<UserSocialAction> selectList(UserSocialAction query) {
        QueryChain<UserSocialAction> chain = this.queryChain();
        if (query.getTargetUserId() != null) {
            chain.eq(UserSocialAction::getTargetUserId, query.getTargetUserId());
        }
        if (query.getActorUserId() != null) {
            chain.eq(UserSocialAction::getActorUserId, query.getActorUserId());
        }
        if (query.getActionType() != null && !query.getActionType().isEmpty()) {
            chain.eq(UserSocialAction::getActionType, query.getActionType());
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            chain.eq(UserSocialAction::getStatus, query.getStatus());
        }
        return chain;
    }

    @Override
    public Page<UserSocialAction> page(UserSocialAction query, int pageNum, int pageSize) {
        return selectList(query).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserSocialAction query, HttpServletResponse response) {
        List<UserSocialAction> list = selectList(query).list();
        ExcelUtil<UserSocialAction> util = new ExcelUtil<>(UserSocialAction.class);
        util.exportExcel(response, list, "用户主页社交动作数据");
    }

    @Override
    public UserSocialAction findIncludingDeleted(Long targetUserId, Long actorUserId, String actionType) {
        QueryWrapper qw = QueryWrapper.create()
            .from(UserSocialAction.class)
            .eq(UserSocialAction::getTargetUserId, targetUserId)
            .eq(UserSocialAction::getActorUserId, actorUserId)
            .eq(UserSocialAction::getActionType, actionType);
        return LogicDeleteManager.execWithoutLogicDelete(() -> getMapper().selectOneByQuery(qw));
    }

    @Override
    public void restoreIfSoftDeleted(UserSocialAction row) {
        if (row == null) {
            return;
        }
        Integer df = row.getDelFlag();
        if (df != null && df != 0) {
            row.setDelFlag(0);
            updateById(row);
        }
    }
}
