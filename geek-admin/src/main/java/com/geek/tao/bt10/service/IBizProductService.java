package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.BizProduct;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletResponse;

public interface IBizProductService extends IService<BizProduct> {
    Page<BizProduct> page(BizProduct query, int pageNum, int pageSize);
    void export(BizProduct query, HttpServletResponse response);
}
