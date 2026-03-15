package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.DataChangeLogs;
import com.geek.tao.bt10.mapper.DataChangeLogsMapper;
import com.geek.tao.bt10.service.IDataChangeLogsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 通用业务数据变更审计 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class DataChangeLogsServiceImpl extends ServiceImpl<DataChangeLogsMapper, DataChangeLogs> implements IDataChangeLogsService {

    private QueryChain<DataChangeLogs> selectList(DataChangeLogs dataChangeLogs) {
        QueryChain<DataChangeLogs> chain = this.queryChain();
        if (dataChangeLogs.getTableName() != null && !dataChangeLogs.getTableName().isEmpty()) {
            chain.like(DataChangeLogs::getTableName, dataChangeLogs.getTableName());
        }
        if (dataChangeLogs.getRecordId() != null) {
            chain.eq(DataChangeLogs::getRecordId, dataChangeLogs.getRecordId());
        }
        if (dataChangeLogs.getChangeType() != null && !dataChangeLogs.getChangeType().isEmpty()) {
            chain.eq(DataChangeLogs::getChangeType, dataChangeLogs.getChangeType());
        }
        if (dataChangeLogs.getFieldName() != null && !dataChangeLogs.getFieldName().isEmpty()) {
            chain.like(DataChangeLogs::getFieldName, dataChangeLogs.getFieldName());
        }
        if (dataChangeLogs.getOldValue() != null && !dataChangeLogs.getOldValue().isEmpty()) {
            chain.eq(DataChangeLogs::getOldValue, dataChangeLogs.getOldValue());
        }
        if (dataChangeLogs.getNewValue() != null && !dataChangeLogs.getNewValue().isEmpty()) {
            chain.eq(DataChangeLogs::getNewValue, dataChangeLogs.getNewValue());
        }
        if (dataChangeLogs.getChangeReason() != null && !dataChangeLogs.getChangeReason().isEmpty()) {
            chain.eq(DataChangeLogs::getChangeReason, dataChangeLogs.getChangeReason());
        }
        if (dataChangeLogs.getOperatorId() != null) {
            chain.eq(DataChangeLogs::getOperatorId, dataChangeLogs.getOperatorId());
        }
        if (dataChangeLogs.getOperatorType() != null && !dataChangeLogs.getOperatorType().isEmpty()) {
            chain.eq(DataChangeLogs::getOperatorType, dataChangeLogs.getOperatorType());
        }
        if (dataChangeLogs.getIpAddress() != null && !dataChangeLogs.getIpAddress().isEmpty()) {
            chain.eq(DataChangeLogs::getIpAddress, dataChangeLogs.getIpAddress());
        }
        if (dataChangeLogs.getUserAgent() != null && !dataChangeLogs.getUserAgent().isEmpty()) {
            chain.eq(DataChangeLogs::getUserAgent, dataChangeLogs.getUserAgent());
        }
        if (dataChangeLogs.getChangedTime() != null) {
            chain.eq(DataChangeLogs::getChangedTime, dataChangeLogs.getChangedTime());
        }
        if (dataChangeLogs.getMetadata() != null && !dataChangeLogs.getMetadata().isEmpty()) {
            chain.eq(DataChangeLogs::getMetadata, dataChangeLogs.getMetadata());
        }
        return chain;
    }

    @Override
    public Page<DataChangeLogs> page(DataChangeLogs dataChangeLogs, int pageNum, int pageSize) {
        return selectList(dataChangeLogs).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(DataChangeLogs dataChangeLogs, HttpServletResponse response) {
        List<DataChangeLogs> list = selectList(dataChangeLogs).list();
        ExcelUtil<DataChangeLogs> util = new ExcelUtil<>(DataChangeLogs.class);
        util.exportExcel(response, list, "通用业务数据变更审计数据");
    }


}
