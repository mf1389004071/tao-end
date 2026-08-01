package com.geek.tao.bt10.service.impl;

import org.springframework.stereotype.Service;

import com.geek.tao.bt10.domain.XiaoeMaterials;
import com.geek.tao.bt10.mapper.XiaoeMaterialsMapper;
import com.geek.tao.bt10.service.IXiaoeMaterialsService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 小鹅通素材内容 服务实现
 */
@Service
public class XiaoeMaterialsServiceImpl extends ServiceImpl<XiaoeMaterialsMapper, XiaoeMaterials>
        implements IXiaoeMaterialsService {
}
