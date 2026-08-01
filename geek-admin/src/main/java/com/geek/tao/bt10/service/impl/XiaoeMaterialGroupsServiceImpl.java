package com.geek.tao.bt10.service.impl;

import org.springframework.stereotype.Service;

import com.geek.tao.bt10.domain.XiaoeMaterialGroups;
import com.geek.tao.bt10.mapper.XiaoeMaterialGroupsMapper;
import com.geek.tao.bt10.service.IXiaoeMaterialGroupsService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 小鹅通素材分组 服务实现
 */
@Service
public class XiaoeMaterialGroupsServiceImpl extends ServiceImpl<XiaoeMaterialGroupsMapper, XiaoeMaterialGroups>
        implements IXiaoeMaterialGroupsService {
}
