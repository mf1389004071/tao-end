package com.geek.tao.bt10.service.impl;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.BizProduct;
import com.geek.tao.bt10.mapper.BizProductMapper;
import com.geek.tao.bt10.service.IBizProductService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizProductServiceImpl extends ServiceImpl<BizProductMapper, BizProduct> implements IBizProductService {

    private QueryChain<BizProduct> selectList(BizProduct q) {
        QueryChain<BizProduct> chain = this.queryChain();
        if (q.getCode() != null && !q.getCode().isEmpty()) {
            chain.eq(BizProduct::getCode, q.getCode());
        }
        if (q.getName() != null && !q.getName().isEmpty()) {
            chain.like(BizProduct::getName, q.getName());
        }
        if (q.getProductType() != null && !q.getProductType().isEmpty()) {
            chain.eq(BizProduct::getProductType, q.getProductType());
        }
        if (q.getIdentityCode() != null && !q.getIdentityCode().isEmpty()) {
            chain.eq(BizProduct::getIdentityCode, q.getIdentityCode());
        }
        if (q.getBizStatus() != null && !q.getBizStatus().isEmpty()) {
            chain.eq(BizProduct::getBizStatus, q.getBizStatus());
        }
        if (q.getStatus() != null && !q.getStatus().isEmpty()) {
            chain.eq(BizProduct::getStatus, q.getStatus());
        }
        return chain.orderBy(BizProduct::getOrderNum, true);
    }

    @Override
    public Page<BizProduct> page(BizProduct query, int pageNum, int pageSize) {
        return selectList(query).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(BizProduct query, HttpServletResponse response) {
        List<BizProduct> list = selectList(query).list();
        ExcelUtil<BizProduct> util = new ExcelUtil<>(BizProduct.class);
        util.exportExcel(response, list, "业务产品");
    }
}
