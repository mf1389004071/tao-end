package com.geek.tao.bt10.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.exception.ServiceException;
import com.geek.system.service.ISysUserService;
import com.geek.tao.bt10.domain.XiaoeOrders;
import com.geek.tao.bt10.domain.XiaoeUserMapping;
import com.geek.tao.bt10.domain.XeknowReportEncryptedReq;
import com.geek.tao.bt10.mapper.XiaoeUserMappingMapper;
import com.geek.tao.bt10.service.IXiaoeOrdersService;
import com.geek.tao.bt10.service.IXiaoeUserMappingService;
import com.geek.tao.bt10.service.IXeknowAdminService;
import com.geek.tao.bt10.xeknow.XeknowCryptoHelper;
import com.geek.tao.bt10.xeknow.XeknowProperties;
import com.mybatisflex.core.query.QueryWrapper;

/**
 * xeknow 采集上报：解密 → 订单/用户落库
 */
@Service
public class XeknowAdminServiceImpl implements IXeknowAdminService {

    private static final Logger log = LoggerFactory.getLogger(XeknowAdminServiceImpl.class);

    private static final String SOURCE_ORDER = "order_list";
    private static final String SOURCE_USER = "user_list";
    private static final DateTimeFormatter XE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private XeknowCryptoHelper cryptoHelper;
    @Autowired
    private XeknowProperties xeknowProperties;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IXiaoeOrdersService xiaoeOrdersService;
    @Autowired
    private IXiaoeUserMappingService xiaoeUserMappingService;
    @Autowired
    private XiaoeUserMappingMapper xiaoeUserMappingMapper;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> ingestReport(XeknowReportEncryptedReq req) {
        if (StringUtils.hasText(req.getKid())
                && StringUtils.hasText(xeknowProperties.getKid())
                && !xeknowProperties.getKid().equals(req.getKid())) {
            throw new ServiceException("kid 不匹配");
        }

        String json = cryptoHelper.decryptToJson(req);
        JsonNode root;
        try {
            root = objectMapper.readTree(json);
        } catch (Exception e) {
            throw new ServiceException("上报明文不是合法 JSON");
        }

        String source = text(root, "source");
        if (!StringUtils.hasText(source)) {
            throw new ServiceException("envelope.source 不能为空");
        }

        int inserted = 0;
        int updated = 0;
        if (SOURCE_ORDER.equals(source)) {
            int[] r = upsertOrders(root);
            inserted = r[0];
            updated = r[1];
        } else if (SOURCE_USER.equals(source)) {
            int[] r = upsertUsers(root);
            inserted = r[0];
            updated = r[1];
        } else {
            throw new ServiceException("不支持的 source: " + source);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ok", true);
        data.put("source", source);
        data.put("page", root.path("page").asInt(0));
        data.put("inserted", inserted);
        data.put("updated", updated);
        data.put("batchId", text(root, "batch_id"));
        return data;
    }

    @Override
    public Map<String, Object> listPhoneMissingUserIds(int limit) {
        int size = limit > 0 ? Math.min(limit, 50) : 10;
        Instant now = Instant.now();
        QueryWrapper qw = QueryWrapper.create()
                .from(XiaoeUserMapping.class)
                .where("del_flag = 0")
                .and("(phone IS NULL OR phone = '')")
                .and("(phone_next_query_at IS NULL OR phone_next_query_at <= ?)", now)
                .orderBy("phone_next_query_at", true)
                .limit(size);
        List<XiaoeUserMapping> rows = xiaoeUserMappingMapper.selectListByQuery(qw);

        List<String> userIds = new ArrayList<>();
        for (XiaoeUserMapping row : rows) {
            if (StringUtils.hasText(row.getXiaoeUserId())) {
                userIds.add(row.getXiaoeUserId());
            }
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ok", true);
        data.put("userIds", userIds);
        data.put("limit", size);
        return data;
    }

    private int[] upsertOrders(JsonNode envelope) {
        JsonNode orderList = envelope.path("payload").path("data").path("order_list");
        if (!orderList.isArray()) {
            throw new ServiceException("order_list 缺失");
        }
        int inserted = 0;
        int updated = 0;
        for (JsonNode item : orderList) {
            String orderNo = text(item, "order_id");
            if (!StringUtils.hasText(orderNo)) {
                continue;
            }
            XiaoeOrders existing = xiaoeOrdersService.queryChain()
                    .eq(XiaoeOrders::getXiaoeOrderNo, orderNo)
                    .one();

            XiaoeOrders row = existing != null ? existing : new XiaoeOrders();
            row.setXiaoeOrderNo(orderNo);
            row.setTradeNo(text(item, "trade_id"));
            row.setOrderState(asState(item.get("order_state")));
            row.setPayState(asState(item.get("pay_state")));
            row.setPayType(text(item, "pay_type_description"));
            if (!StringUtils.hasText(row.getPayType())) {
                row.setPayType(asState(item.get("pay_type")));
            }
            row.setActualFee(centsToYuan(item.get("actual_fee")));
            row.setXiaoeCreateTime(parseXeTime(text(item, "create_at")));
            fillGoods(row, item.path("goods_list"));
            row.setStudentInfo(toJson(item.get("student_info")));
            row.setInvoiceInfo(toJson(item.get("invoice_info")));
            row.setJsonData(toJson(item));
            row.setSyncStatus("SYNCED");
            row.setLastSyncTime(Instant.now());
            row.setDelFlag(0);
            if (!StringUtils.hasText(row.getStatus())) {
                row.setStatus("0");
            }

            String xeUserId = text(item, "user_id");
            if (StringUtils.hasText(xeUserId)) {
                XiaoeUserMapping mapping = xiaoeUserMappingService.queryChain()
                        .eq(XiaoeUserMapping::getXiaoeUserId, xeUserId)
                        .one();
                if (mapping != null && mapping.getUserId() != null) {
                    row.setUserId(mapping.getUserId());
                }
            }

            if (existing == null) {
                xiaoeOrdersService.save(row);
                inserted++;
            } else {
                xiaoeOrdersService.updateById(row);
                updated++;
            }
        }
        return new int[] { inserted, updated };
    }

    private int[] upsertUsers(JsonNode envelope) {
        JsonNode list = envelope.path("payload").path("data").path("list");
        JsonNode phones = envelope.path("payload").path("phones");
        if (!list.isArray()) {
            throw new ServiceException("user list 缺失");
        }
        int inserted = 0;
        int updated = 0;
        String batchId = text(envelope, "batch_id");
        String capturedAt = text(envelope, "captured_at");
        Instant retryAt = Instant.now().plus(xeknowProperties.getPhoneRetryDays(), ChronoUnit.DAYS);

        for (JsonNode item : list) {
            String xeId = text(item, "user_id");
            if (!StringUtils.hasText(xeId)) {
                continue;
            }
            String phone = "";
            if (phones != null && phones.has(xeId) && !phones.get(xeId).isNull()) {
                phone = phones.get(xeId).asText("");
            }
            phone = phone == null ? "" : phone.trim();

            String nick = text(item, "user_name");
            if (!StringUtils.hasText(nick)) {
                nick = text(item, "comment_name");
            }

            ObjectNode snap = objectMapper.createObjectNode();
            snap.set("list_item", item);
            snap.put("phone", phone);
            if (StringUtils.hasText(capturedAt)) {
                snap.put("captured_at", capturedAt);
            }
            if (StringUtils.hasText(batchId)) {
                snap.put("batch_id", batchId);
            }
            String jsonData = toJson(snap);

            XiaoeUserMapping existing = xiaoeUserMappingService.queryChain()
                    .eq(XiaoeUserMapping::getXiaoeUserId, xeId)
                    .one();

            if (existing == null) {
                XiaoeUserMapping row = new XiaoeUserMapping();
                row.setXiaoeUserId(xeId);
                row.setNickName(nick);
                row.setJsonData(jsonData);
                row.setMappingType("AUTO");
                row.setMappedTime(Instant.now());
                row.setDelFlag(0);
                row.setStatus("0");
                applyPhoneOnWrite(row, phone, true, retryAt);
                xiaoeUserMappingService.save(row);
                inserted++;
            } else {
                existing.setNickName(nick);
                existing.setJsonData(jsonData);
                applyPhoneOnWrite(existing, phone, false, retryAt);
                xiaoeUserMappingService.updateById(existing);
                maybeWriteSysUserPhone(existing, phone);
                updated++;
            }
        }
        return new int[] { inserted, updated };
    }

    /**
     * 有号：写入并清空延期；空号：不覆盖已有真号，仍缺号则 +N 天
     */
    private void applyPhoneOnWrite(XiaoeUserMapping row, String phone, boolean isNew, Instant retryAt) {
        boolean hasPhone = StringUtils.hasText(phone);
        if (hasPhone) {
            row.setPhone(phone);
            row.setPhoneNextQueryAt(null);
            return;
        }
        // 空号
        if (StringUtils.hasText(row.getPhone())) {
            // 保留历史真号，不延期
            return;
        }
        row.setPhone("");
        row.setPhoneNextQueryAt(retryAt);
        if (isNew) {
            log.debug("xeknow new user missing phone, nextQueryAt={}", retryAt);
        }
    }

    private void maybeWriteSysUserPhone(XiaoeUserMapping mapping, String phone) {
        if (!StringUtils.hasText(phone) || mapping.getUserId() == null) {
            return;
        }
        SysUser user = sysUserService.selectUserById(mapping.getUserId());
        if (user == null) {
            return;
        }
        if (StringUtils.hasText(user.getPhonenumber())) {
            return;
        }
        SysUser patch = new SysUser();
        patch.setUserId(user.getUserId());
        patch.setPhonenumber(phone);
        sysUserService.updateUserProfile(patch);
    }

    private void fillGoods(XiaoeOrders row, JsonNode goodsList) {
        if (!goodsList.isArray() || goodsList.isEmpty()) {
            return;
        }
        JsonNode g0 = goodsList.get(0);
        row.setGoodsName(text(g0, "goods_name"));
        row.setSpuType(text(g0, "spu_type"));
        String typeDesc = text(g0, "spu_type_description");
        row.setGoodsType(StringUtils.hasText(typeDesc) ? typeDesc : text(g0, "relation_goods_type_description"));
    }

    private static BigDecimal centsToYuan(JsonNode node) {
        if (node == null || node.isNull() || !node.isNumber()) {
            return null;
        }
        return BigDecimal.valueOf(node.asLong())
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }

    private static Instant parseXeTime(String raw) {
        if (!StringUtils.hasText(raw)) {
            return null;
        }
        try {
            LocalDateTime ldt = LocalDateTime.parse(raw.trim(), XE_TIME);
            return ldt.atZone(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            try {
                return Instant.parse(raw.trim());
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    private static String asState(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        return node.asText();
    }

    private static String text(JsonNode node, String field) {
        if (node == null) {
            return null;
        }
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) {
            return null;
        }
        String s = v.asText();
        return s == null || s.isBlank() ? null : s.trim();
    }

    private String toJson(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            return null;
        }
    }
}
