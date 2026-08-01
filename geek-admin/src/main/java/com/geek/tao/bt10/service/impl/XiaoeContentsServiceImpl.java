package com.geek.tao.bt10.service.impl;

import org.springframework.stereotype.Service;

import com.geek.tao.bt10.domain.XiaoeContents;
import com.geek.tao.bt10.mapper.XiaoeContentsMapper;
import com.geek.tao.bt10.service.IXiaoeContentsService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 小鹅通内容资源同步表 服务实现
 */
@Service
public class XiaoeContentsServiceImpl extends ServiceImpl<XiaoeContentsMapper, XiaoeContents>
        implements IXiaoeContentsService {
}
