package com.geek.tao.bt10.service.impl;

import org.springframework.stereotype.Service;

import com.geek.tao.bt10.domain.XiaoeSvipMembers;
import com.geek.tao.bt10.mapper.XiaoeSvipMembersMapper;
import com.geek.tao.bt10.service.IXiaoeSvipMembersService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

/**
 * 小鹅通超级会员成员 服务实现
 */
@Service
public class XiaoeSvipMembersServiceImpl extends ServiceImpl<XiaoeSvipMembersMapper, XiaoeSvipMembers>
        implements IXiaoeSvipMembersService {
}
