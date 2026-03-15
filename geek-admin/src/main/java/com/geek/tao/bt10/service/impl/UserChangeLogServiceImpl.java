package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.UserChangeLog;
import com.geek.tao.bt10.mapper.UserChangeLogMapper;
import com.geek.tao.bt10.service.IUserChangeLogService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户关键字段变更记录 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class UserChangeLogServiceImpl extends ServiceImpl<UserChangeLogMapper, UserChangeLog> implements IUserChangeLogService {

    private QueryChain<UserChangeLog> selectList(UserChangeLog userChangeLog) {
        QueryChain<UserChangeLog> chain = this.queryChain();
        if (userChangeLog.getUserId() != null) {
            chain.eq(UserChangeLog::getUserId, userChangeLog.getUserId());
        }
        if (userChangeLog.getChangeField() != null && !userChangeLog.getChangeField().isEmpty()) {
            chain.eq(UserChangeLog::getChangeField, userChangeLog.getChangeField());
        }
        if (userChangeLog.getOldValue() != null && !userChangeLog.getOldValue().isEmpty()) {
            chain.eq(UserChangeLog::getOldValue, userChangeLog.getOldValue());
        }
        if (userChangeLog.getNewValue() != null && !userChangeLog.getNewValue().isEmpty()) {
            chain.eq(UserChangeLog::getNewValue, userChangeLog.getNewValue());
        }
        if (userChangeLog.getChangeReason() != null && !userChangeLog.getChangeReason().isEmpty()) {
            chain.eq(UserChangeLog::getChangeReason, userChangeLog.getChangeReason());
        }
        if (userChangeLog.getOperatorId() != null) {
            chain.eq(UserChangeLog::getOperatorId, userChangeLog.getOperatorId());
        }
        if (userChangeLog.getOperatorType() != null && !userChangeLog.getOperatorType().isEmpty()) {
            chain.eq(UserChangeLog::getOperatorType, userChangeLog.getOperatorType());
        }
        if (userChangeLog.getChangedTime() != null) {
            chain.eq(UserChangeLog::getChangedTime, userChangeLog.getChangedTime());
        }
        if (userChangeLog.getMetadata() != null && !userChangeLog.getMetadata().isEmpty()) {
            chain.eq(UserChangeLog::getMetadata, userChangeLog.getMetadata());
        }
        return chain;
    }

    @Override
    public Page<UserChangeLog> page(UserChangeLog userChangeLog, int pageNum, int pageSize) {
        return selectList(userChangeLog).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(UserChangeLog userChangeLog, HttpServletResponse response) {
        List<UserChangeLog> list = selectList(userChangeLog).list();
        ExcelUtil<UserChangeLog> util = new ExcelUtil<>(UserChangeLog.class);
        util.exportExcel(response, list, "用户关键字段变更记录数据");
    }


}
