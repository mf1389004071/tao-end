package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.AiVectors;
import com.geek.tao.bt10.mapper.AiVectorsMapper;
import com.geek.tao.bt10.service.IAiVectorsService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * AI向量 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class AiVectorsServiceImpl extends ServiceImpl<AiVectorsMapper, AiVectors> implements IAiVectorsService {

    private QueryChain<AiVectors> selectList(AiVectors aiVectors) {
        QueryChain<AiVectors> chain = this.queryChain();
        if (aiVectors.getContentChunk() != null && !aiVectors.getContentChunk().isEmpty()) {
            chain.eq(AiVectors::getContentChunk, aiVectors.getContentChunk());
        }
        if (aiVectors.getEmbedding() != null && !aiVectors.getEmbedding().isEmpty()) {
            chain.eq(AiVectors::getEmbedding, aiVectors.getEmbedding());
        }
        if (aiVectors.getMetadata() != null && !aiVectors.getMetadata().isEmpty()) {
            chain.eq(AiVectors::getMetadata, aiVectors.getMetadata());
        }
        if (aiVectors.getSourceType() != null && !aiVectors.getSourceType().isEmpty()) {
            chain.eq(AiVectors::getSourceType, aiVectors.getSourceType());
        }
        if (aiVectors.getSourceId() != null) {
            chain.eq(AiVectors::getSourceId, aiVectors.getSourceId());
        }
        if (aiVectors.getCreatedAt() != null) {
            chain.eq(AiVectors::getCreatedAt, aiVectors.getCreatedAt());
        }
        if (aiVectors.getUpdatedAt() != null) {
            chain.eq(AiVectors::getUpdatedAt, aiVectors.getUpdatedAt());
        }
        return chain;
    }

    @Override
    public Page<AiVectors> page(AiVectors aiVectors, int pageNum, int pageSize) {
        return selectList(aiVectors).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(AiVectors aiVectors, HttpServletResponse response) {
        List<AiVectors> list = selectList(aiVectors).list();
        ExcelUtil<AiVectors> util = new ExcelUtil<>(AiVectors.class);
        util.exportExcel(response, list, "AI向量数据");
    }


}
