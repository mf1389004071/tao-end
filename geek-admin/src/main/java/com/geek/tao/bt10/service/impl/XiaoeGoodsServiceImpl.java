package com.geek.tao.bt10.service.impl;

import org.springframework.stereotype.Service;

import com.geek.tao.bt10.domain.XiaoeGoods;
import com.geek.tao.bt10.mapper.XiaoeGoodsMapper;
import com.geek.tao.bt10.service.IXiaoeGoodsService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

@Service
public class XiaoeGoodsServiceImpl extends ServiceImpl<XiaoeGoodsMapper, XiaoeGoods>
        implements IXiaoeGoodsService {
}
