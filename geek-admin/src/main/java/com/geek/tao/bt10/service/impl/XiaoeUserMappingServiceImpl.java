package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.XiaoeUserMapping;
import com.geek.tao.bt10.mapper.XiaoeUserMappingMapper;
import com.geek.tao.bt10.service.IXiaoeUserMappingService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 本平台用户与小鹅通用户ID映射 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class XiaoeUserMappingServiceImpl extends ServiceImpl<XiaoeUserMappingMapper, XiaoeUserMapping> implements IXiaoeUserMappingService {

    private QueryChain<XiaoeUserMapping> selectList(XiaoeUserMapping xiaoeUserMapping) {
        QueryChain<XiaoeUserMapping> chain = this.queryChain();
        if (xiaoeUserMapping.getUserId() != null) {
            chain.eq(XiaoeUserMapping::getUserId, xiaoeUserMapping.getUserId());
        }
        if (xiaoeUserMapping.getXiaoeUserId() != null && !xiaoeUserMapping.getXiaoeUserId().isEmpty()) {
            chain.eq(XiaoeUserMapping::getXiaoeUserId, xiaoeUserMapping.getXiaoeUserId());
        }
        if (xiaoeUserMapping.getMappingType() != null && !xiaoeUserMapping.getMappingType().isEmpty()) {
            chain.eq(XiaoeUserMapping::getMappingType, xiaoeUserMapping.getMappingType());
        }
        if (xiaoeUserMapping.getConfidenceScore() != null) {
            chain.eq(XiaoeUserMapping::getConfidenceScore, xiaoeUserMapping.getConfidenceScore());
        }
        if (xiaoeUserMapping.getMappedTime() != null) {
            chain.eq(XiaoeUserMapping::getMappedTime, xiaoeUserMapping.getMappedTime());
        }
        if (xiaoeUserMapping.getLastUpdateTime() != null) {
            chain.eq(XiaoeUserMapping::getLastUpdateTime, xiaoeUserMapping.getLastUpdateTime());
        }
        if (xiaoeUserMapping.getStatus() != null && !xiaoeUserMapping.getStatus().isEmpty()) {
            chain.eq(XiaoeUserMapping::getStatus, xiaoeUserMapping.getStatus());
        }
        return chain;
    }

    @Override
    public Page<XiaoeUserMapping> page(XiaoeUserMapping xiaoeUserMapping, int pageNum, int pageSize) {
        return selectList(xiaoeUserMapping).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(XiaoeUserMapping xiaoeUserMapping, HttpServletResponse response) {
        List<XiaoeUserMapping> list = selectList(xiaoeUserMapping).list();
        ExcelUtil<XiaoeUserMapping> util = new ExcelUtil<>(XiaoeUserMapping.class);
        util.exportExcel(response, list, "本平台用户与小鹅通用户ID映射数据");
    }


}
