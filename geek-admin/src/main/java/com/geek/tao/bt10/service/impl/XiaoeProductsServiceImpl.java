package com.geek.tao.bt10.service.impl;

import org.springframework.stereotype.Service;

import com.geek.tao.bt10.domain.XiaoeProducts;
import com.geek.tao.bt10.mapper.XiaoeProductsMapper;
import com.geek.tao.bt10.service.IXiaoeProductsService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 小鹅通产品资源同步表 服务实现
 */
@Service
public class XiaoeProductsServiceImpl extends ServiceImpl<XiaoeProductsMapper, XiaoeProducts>
        implements IXiaoeProductsService {
}
