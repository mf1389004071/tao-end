package com.geek.tao.bt10.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.tao.bt10.domain.Identities;
import com.geek.tao.bt10.mapper.IdentitiesMapper;
import com.geek.tao.bt10.service.IIdentitiesService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统身份定义表 服务层实现
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Service
public class IdentitiesServiceImpl extends ServiceImpl<IdentitiesMapper, Identities> implements IIdentitiesService {

    private QueryChain<Identities> selectList(Identities identities) {
        QueryChain<Identities> chain = this.queryChain();
        if (identities.getIdentityLevel() != null) {
            chain.eq(Identities::getIdentityLevel, identities.getIdentityLevel());
        }
        if (identities.getName() != null && !identities.getName().isEmpty()) {
            chain.like(Identities::getName, identities.getName());
        }
        if (identities.getIcon() != null && !identities.getIcon().isEmpty()) {
            chain.eq(Identities::getIcon, identities.getIcon());
        }
        if (identities.getThemeColor() != null && !identities.getThemeColor().isEmpty()) {
            chain.eq(Identities::getThemeColor, identities.getThemeColor());
        }
        if (identities.getIntro() != null && !identities.getIntro().isEmpty()) {
            chain.eq(Identities::getIntro, identities.getIntro());
        }
        if (identities.getLegalText() != null && !identities.getLegalText().isEmpty()) {
            chain.eq(Identities::getLegalText, identities.getLegalText());
        }
        if (identities.getRightsText() != null && !identities.getRightsText().isEmpty()) {
            chain.eq(Identities::getRightsText, identities.getRightsText());
        }
        if (identities.getDutiesText() != null && !identities.getDutiesText().isEmpty()) {
            chain.eq(Identities::getDutiesText, identities.getDutiesText());
        }
        if (identities.getBenefitsText() != null && !identities.getBenefitsText().isEmpty()) {
            chain.eq(Identities::getBenefitsText, identities.getBenefitsText());
        }
        if (identities.getUpgradeRulesText() != null && !identities.getUpgradeRulesText().isEmpty()) {
            chain.eq(Identities::getUpgradeRulesText, identities.getUpgradeRulesText());
        }
        if (identities.getPriceAmount() != null) {
            chain.eq(Identities::getPriceAmount, identities.getPriceAmount());
        }
        if (identities.getStatus() != null && !identities.getStatus().isEmpty()) {
            chain.eq(Identities::getStatus, identities.getStatus());
        }
        return chain;
    }

    @Override
    public Page<Identities> page(Identities identities, int pageNum, int pageSize) {
        return selectList(identities).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(Identities identities, HttpServletResponse response) {
        List<Identities> list = selectList(identities).list();
        ExcelUtil<Identities> util = new ExcelUtil<>(Identities.class);
        util.exportExcel(response, list, "系统身份定义表数据");
    }


}
