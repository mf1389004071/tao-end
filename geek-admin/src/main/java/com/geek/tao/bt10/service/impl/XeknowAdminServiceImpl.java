package com.geek.tao.bt10.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
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
import com.geek.tao.bt10.domain.XiaoeContents;
import com.geek.tao.bt10.domain.XiaoeMaterialGroups;
import com.geek.tao.bt10.domain.XiaoeMaterials;
import com.geek.tao.bt10.domain.XiaoeOrders;
import com.geek.tao.bt10.domain.XiaoeProducts;
import com.geek.tao.bt10.domain.XiaoeSvipMembers;
import com.geek.tao.bt10.domain.XiaoeUserMapping;
import com.geek.tao.bt10.domain.XeknowReportEncryptedReq;
import com.geek.tao.bt10.mapper.XiaoeUserMappingMapper;
import com.geek.tao.bt10.service.IXiaoeContentsService;
import com.geek.tao.bt10.service.IXiaoeMaterialGroupsService;
import com.geek.tao.bt10.service.IXiaoeMaterialsService;
import com.geek.tao.bt10.service.IXiaoeOrdersService;
import com.geek.tao.bt10.service.IXiaoeProductsService;
import com.geek.tao.bt10.service.IXiaoeSvipMembersService;
import com.geek.tao.bt10.service.IXiaoeUserMappingService;
import com.geek.tao.bt10.service.IXeknowAdminService;
import com.geek.tao.bt10.xeknow.XeknowCryptoHelper;
import com.geek.tao.bt10.xeknow.XeknowProperties;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;

/**
 * xeknow 采集上报：解密 → 订单/用户/产品落库
 */
@Service
public class XeknowAdminServiceImpl implements IXeknowAdminService {

    private static final Logger log = LoggerFactory.getLogger(XeknowAdminServiceImpl.class);

    private static final String SOURCE_ORDER = "order_list";
    private static final String SOURCE_USER = "user_list";
    private static final String SOURCE_PRODUCT = "product_list";
    private static final String SOURCE_CONTENT = "content_list";
    private static final String SOURCE_SVIP = "svip_list";
    private static final String SOURCE_MATERIAL_GROUP = "material_group";
    private static final String SOURCE_MATERIAL = "material_list";
    private static final DateTimeFormatter XE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter XE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
    private IXiaoeProductsService xiaoeProductsService;
    @Autowired
    private IXiaoeContentsService xiaoeContentsService;
    @Autowired
    private IXiaoeSvipMembersService xiaoeSvipMembersService;
    @Autowired
    private IXiaoeMaterialGroupsService xiaoeMaterialGroupsService;
    @Autowired
    private IXiaoeMaterialsService xiaoeMaterialsService;
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
        } else if (SOURCE_PRODUCT.equals(source)) {
            int[] r = upsertProducts(root);
            inserted = r[0];
            updated = r[1];
        } else if (SOURCE_CONTENT.equals(source)) {
            int[] r = upsertContents(root);
            inserted = r[0];
            updated = r[1];
        } else if (SOURCE_SVIP.equals(source)) {
            int[] r = upsertSvipMembers(root);
            inserted = r[0];
            updated = r[1];
        } else if (SOURCE_MATERIAL_GROUP.equals(source)) {
            int[] r = upsertMaterialGroups(root);
            inserted = r[0];
            updated = r[1];
        } else if (SOURCE_MATERIAL.equals(source)) {
            int[] r = upsertMaterials(root);
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
        if (SOURCE_PRODUCT.equals(source)) {
            data.put("resourceType", root.path("resource_type").asInt(0));
            data.put("productKind", text(root, "product_kind"));
        }
        if (SOURCE_CONTENT.equals(source)) {
            data.put("resourceType", root.path("resource_type").asInt(0));
            data.put("contentKind", text(root, "content_kind"));
        }
        if (SOURCE_SVIP.equals(source)) {
            data.put("svipId", text(root, "svip_id"));
            data.put("svipTitle", text(root, "svip_title"));
        }
        if (SOURCE_MATERIAL_GROUP.equals(source) || SOURCE_MATERIAL.equals(source)) {
            data.put("materialType", root.path("material_type").asInt(0));
            data.put("materialKind", text(root, "material_kind"));
        }
        return data;
    }

    @Override
    public Map<String, Object> listPhoneMissingUserIds(int page, int pageSize) {
        int size = pageSize > 0 ? Math.min(pageSize, 200) : 100;
        int p = page < 1 ? 1 : page;
        Instant now = Instant.now();
        // 仅「有效手机号」算已有号；null / '' / 掩码 / 非法格式都进重查队列
        QueryWrapper countQw = QueryWrapper.create()
                .from(XiaoeUserMapping.class)
                .where("del_flag = 0")
                .and("(phone IS NULL OR btrim(phone) = '' OR phone !~ '^1[3-9][0-9]{9}$')")
                .and("(phone_next_query_at IS NULL OR phone_next_query_at <= ?)", now);
        long total = xiaoeUserMappingMapper.selectCountByQuery(countQw);

        QueryWrapper qw = QueryWrapper.create()
                .from(XiaoeUserMapping.class)
                .where("del_flag = 0")
                .and("(phone IS NULL OR btrim(phone) = '' OR phone !~ '^1[3-9][0-9]{9}$')")
                .and("(phone_next_query_at IS NULL OR phone_next_query_at <= ?)", now)
                .orderBy("phone_next_query_at", true)
                .limit(size)
                .offset((long) (p - 1) * size);
        List<XiaoeUserMapping> rows = xiaoeUserMappingMapper.selectListByQuery(qw);

        List<String> userIds = new ArrayList<>();
        for (XiaoeUserMapping row : rows) {
            if (StringUtils.hasText(row.getXiaoeUserId())) {
                userIds.add(row.getXiaoeUserId());
            }
        }

        int totalPages = total <= 0 ? 0 : (int) ((total + size - 1) / size);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ok", true);
        data.put("userIds", userIds);
        data.put("page", p);
        data.put("pageSize", size);
        // 用 int，避免全局 Long→String 序列化把分页总数变成字符串
        data.put("total", (int) Math.min(total, Integer.MAX_VALUE));
        data.put("totalPages", totalPages);
        // 兼容旧字段
        data.put("limit", size);
        return data;
    }

    private int[] upsertProducts(JsonNode envelope) {
        JsonNode list = envelope.path("payload").path("data").path("list");
        if (!list.isArray()) {
            throw new ServiceException("product list 缺失");
        }
        String envelopeKind = text(envelope, "product_kind");
        Integer envelopeRt = intOrNull(envelope.get("resource_type"));
        String envelopeAppId = text(envelope, "app_id");
        if (!StringUtils.hasText(envelopeAppId)) {
            envelopeAppId = text(envelope.path("payload").path("data"), "app_id");
        }

        int inserted = 0;
        int updated = 0;
        for (JsonNode item : list) {
            String resourceId = text(item, "resource_id");
            if (!StringUtils.hasText(resourceId)) {
                continue;
            }
            XiaoeProducts existing = xiaoeProductsService.queryChain()
                    .eq(XiaoeProducts::getResourceId, resourceId)
                    .one();
            XiaoeProducts row = existing != null ? existing : new XiaoeProducts();
            fillProductRow(row, item, envelopeKind, envelopeRt, envelopeAppId);
            if (existing == null) {
                xiaoeProductsService.save(row);
                inserted++;
            } else {
                xiaoeProductsService.updateById(row);
                updated++;
            }
        }
        return new int[] { inserted, updated };
    }

    private void fillProductRow(XiaoeProducts row, JsonNode item, String envelopeKind,
            Integer envelopeRt, String envelopeAppId) {
        Integer resourceType = intOrNull(item.get("resource_type"));
        if (resourceType == null) {
            resourceType = envelopeRt;
        }
        row.setResourceId(text(item, "resource_id"));
        row.setResourceType(resourceType);
        row.setProductKind(resolveProductKind(envelopeKind, resourceType));
        row.setAppId(StringUtils.hasText(envelopeAppId) ? envelopeAppId : text(item, "app_id"));
        row.setTitle(text(item, "title"));
        row.setSummary(text(item, "summary"));
        row.setImgUrl(text(item, "img_url"));
        row.setImgUrlCompressed(text(item, "img_url_compressed"));
        row.setH5Url(text(item, "h5_url"));
        row.setPrice(centsToYuan(item.get("price")));
        row.setLinePrice(centsToYuan(item.get("line_price")));
        row.setGoodsType(intOrNull(item.get("goods_type")));
        row.setSellType(intOrNull(item.get("sell_type")));
        row.setSaleStatus(intOrNull(item.get("sale_status")));
        row.setAuditStatus(intOrNull(item.get("audit_status")));
        row.setAuthType(intOrNull(item.get("auth_type")));
        row.setProtectStatus(intOrNull(item.get("protect_status")));
        row.setVersionId(text(item, "version_id"));
        row.setPosition(intOrNull(item.get("position")));
        row.setIsFree(intOrNull(item.get("is_free")));
        row.setIsPublic(intOrNull(item.get("is_public")));
        row.setIsPassword(intOrNull(item.get("is_password")));
        row.setIsStopSell(intOrNull(item.get("is_stop_sell")));
        row.setIsDisplay(intOrNull(item.get("is_display")));
        row.setIsBan(intOrNull(item.get("is_ban")));
        row.setIsForceUnshelve(intOrNull(item.get("is_force_unshelve")));
        row.setIsJoinMarketAct(intOrNull(item.get("is_join_market_act")));
        row.setIsTranscode(intOrNull(item.get("is_transcode")));
        row.setSaleAt(parseFlexibleTime(item.get("sale_at")));
        row.setCanSoldStart(parseFlexibleTime(item.get("can_sold_start")));
        row.setCanSoldEnd(parseFlexibleTime(item.get("can_sold_end")));

        JsonNode period = item.get("period");
        if (period != null && period.isObject()) {
            row.setPeriodType(intOrNull(period.get("period_type")));
            row.setPeriodValue(text(period, "period_value"));
            row.setPeriodJson(toJson(period));
        } else {
            row.setPeriodType(null);
            row.setPeriodValue(null);
            row.setPeriodJson(null);
        }

        row.setUserCount(intOrNull(item.get("user_count")));
        row.setViewCount(intOrNull(item.get("view_count")));
        row.setResourceCnt(intOrNull(item.get("resource_cnt")));
        row.setInteractiveCnt(intOrNull(item.get("interactive_cnt")));
        row.setSubCourseCnt(intOrNull(item.get("sub_course_cnt")));
        row.setLastUpdatedAt(parseFlexibleTime(item.get("last_updated_at")));
        row.setCurriculumTime(parseFlexibleTime(item.get("curriculum_time")));
        row.setCurriculumEndTime(parseFlexibleTime(item.get("curriculum_end_time")));
        row.setCreatedSource(intOrNull(item.get("created_source")));
        row.setBelongUserInfo(toJson(item.get("belong_user_info")));
        row.setCreatedByResourceInfo(toJson(item.get("created_by_resource_info")));
        row.setJsonData(toJson(item));
        row.setSyncStatus("SYNCED");
        row.setLastSyncTime(Instant.now());
        row.setDelFlag(0);
        if (!StringUtils.hasText(row.getStatus())) {
            row.setStatus("0");
        }
    }

    private static String resolveProductKind(String envelopeKind, Integer resourceType) {
        if (StringUtils.hasText(envelopeKind)) {
            return envelopeKind.trim().toUpperCase();
        }
        if (resourceType == null) {
            return null;
        }
        return switch (resourceType) {
            case 50 -> "CAMP_PRO";
            case 6 -> "COLUMN";
            case 8 -> "BIG_COLUMN";
            default -> "RT_" + resourceType;
        };
    }

    private int[] upsertContents(JsonNode envelope) {
        JsonNode list = envelope.path("payload").path("data").path("list");
        if (!list.isArray()) {
            throw new ServiceException("content list 缺失");
        }
        String envelopeKind = text(envelope, "content_kind");
        Integer envelopeRt = intOrNull(envelope.get("resource_type"));
        String envelopeAppId = text(envelope, "app_id");

        int inserted = 0;
        int updated = 0;
        for (JsonNode item : list) {
            String resourceId = text(item, "resource_id");
            if (!StringUtils.hasText(resourceId)) {
                continue;
            }
            XiaoeContents existing = xiaoeContentsService.queryChain()
                    .eq(XiaoeContents::getResourceId, resourceId)
                    .one();
            XiaoeContents row = existing != null ? existing : new XiaoeContents();
            fillContentRow(row, item, envelopeKind, envelopeRt, envelopeAppId);
            if (existing == null) {
                xiaoeContentsService.save(row);
                inserted++;
            } else {
                xiaoeContentsService.updateById(row);
                updated++;
            }
        }
        return new int[] { inserted, updated };
    }

    private void fillContentRow(XiaoeContents row, JsonNode item, String envelopeKind,
            Integer envelopeRt, String envelopeAppId) {
        Integer resourceType = intOrNull(item.get("resource_type"));
        if (resourceType == null) {
            resourceType = envelopeRt;
        }
        row.setResourceId(text(item, "resource_id"));
        row.setResourceType(resourceType);
        row.setContentKind(resolveContentKind(envelopeKind, resourceType));
        row.setAppId(StringUtils.hasText(envelopeAppId) ? envelopeAppId : text(item, "app_id"));
        row.setTitle(text(item, "title"));
        row.setImgUrl(text(item, "img_url"));
        row.setImgUrlCompressed(text(item, "img_url_compressed"));
        row.setH5Url(text(item, "h5_url"));
        row.setPrice(centsToYuan(item.get("price")));
        row.setLinePrice(centsToYuan(item.get("line_price")));
        row.setGoodsType(intOrNull(item.get("goods_type")));
        row.setSellType(intOrNull(item.get("sell_type")));
        row.setSaleStatus(intOrNull(item.get("sale_status")));
        row.setAuditStatus(intOrNull(item.get("audit_status")));
        row.setAuthType(intOrNull(item.get("auth_type")));
        row.setProtectStatus(intOrNull(item.get("protect_status")));
        row.setVersionId(text(item, "version_id"));
        row.setPosition(intOrNull(item.get("position")));
        row.setViewCount(intOrNull(item.get("view_count")));
        row.setIsFree(intOrNull(item.get("is_free")));
        row.setIsPublic(intOrNull(item.get("is_public")));
        row.setIsPassword(intOrNull(item.get("is_password")));
        row.setIsStopSell(intOrNull(item.get("is_stop_sell")));
        row.setIsDisplay(intOrNull(item.get("is_display")));
        row.setIsBan(intOrNull(item.get("is_ban")));
        row.setIsForceUnshelve(intOrNull(item.get("is_force_unshelve")));
        row.setIsJoinMarketAct(intOrNull(item.get("is_join_market_act")));
        row.setIsTranscode(intOrNull(item.get("is_transcode")));
        row.setSaleAt(parseFlexibleTime(item.get("sale_at")));

        JsonNode period = item.get("period");
        if (period != null && period.isObject()) {
            row.setPeriodType(intOrNull(period.get("period_type")));
            row.setPeriodValue(text(period, "period_value"));
            row.setPeriodJson(toJson(period));
        } else {
            row.setPeriodType(null);
            row.setPeriodValue(null);
            row.setPeriodJson(null);
        }

        row.setJsonData(toJson(item));
        row.setSyncStatus("SYNCED");
        row.setLastSyncTime(Instant.now());
        row.setDelFlag(0);
        if (!StringUtils.hasText(row.getStatus())) {
            row.setStatus("0");
        }
    }

    private static String resolveContentKind(String envelopeKind, Integer resourceType) {
        if (StringUtils.hasText(envelopeKind)) {
            return envelopeKind.trim().toUpperCase();
        }
        if (resourceType == null) {
            return null;
        }
        return switch (resourceType) {
            case 1 -> "TEXT";
            case 2 -> "AUDIO";
            case 3 -> "VIDEO";
            default -> "RT_" + resourceType;
        };
    }

    private int[] upsertMaterialGroups(JsonNode envelope) {
        JsonNode list = envelope.path("payload").path("data").path("list");
        if (!list.isArray()) {
            throw new ServiceException("material group list 缺失");
        }
        Integer materialType = intOrNull(envelope.get("material_type"));
        String materialKind = text(envelope, "material_kind");
        String appId = text(envelope, "app_id");
        Integer typeCount = intOrNull(envelope.path("payload").path("data").get("type_count"));
        if (typeCount == null) {
            typeCount = intOrNull(envelope.get("type_count"));
        }
        int inserted = 0;
        int updated = 0;
        for (JsonNode item : flattenMaterialGroups(list)) {
            Long categoryId = longOrNull(item.get("id"));
            Integer type = materialType != null ? materialType : intOrNull(item.get("type"));
            if (categoryId == null || type == null) {
                continue;
            }
            XiaoeMaterialGroups existing = xiaoeMaterialGroupsService.queryChain()
                    .eq(XiaoeMaterialGroups::getMaterialType, type)
                    .eq(XiaoeMaterialGroups::getCategoryId, categoryId)
                    .one();
            XiaoeMaterialGroups row = existing != null ? existing : new XiaoeMaterialGroups();
            row.setCategoryId(categoryId);
            row.setMaterialType(type);
            row.setMaterialKind(resolveMaterialKind(materialKind, type));
            row.setAppId(StringUtils.hasText(appId) ? appId : text(item, "app_id"));
            if ("*".equals(row.getAppId())) {
                row.setAppId(appId);
            }
            row.setParentId(longOrNull(item.get("parent_id")));
            row.setName(text(item, "name"));
            row.setCategorySort(text(item, "category_sort"));
            Integer cnt = intOrNull(item.get("category_count"));
            if (cnt == null && item.has("category_count") && item.get("category_count").isTextual()) {
                try {
                    cnt = Integer.parseInt(item.get("category_count").asText().trim());
                } catch (Exception ignored) {
                    cnt = null;
                }
            }
            row.setCategoryCount(cnt);
            row.setTypeCount(typeCount);
            row.setJsonData(toJson(item));
            row.setSyncStatus("SYNCED");
            row.setLastSyncTime(Instant.now());
            row.setDelFlag(0);
            if (!StringUtils.hasText(row.getStatus())) {
                row.setStatus("0");
            }
            if (existing == null) {
                xiaoeMaterialGroupsService.save(row);
                inserted++;
            } else {
                xiaoeMaterialGroupsService.updateById(row);
                updated++;
            }
        }
        return new int[] { inserted, updated };
    }

    private static List<JsonNode> flattenMaterialGroups(JsonNode list) {
        List<JsonNode> out = new ArrayList<>();
        if (list == null || !list.isArray()) {
            return out;
        }
        for (JsonNode n : list) {
            out.add(n);
            JsonNode children = n.get("children");
            if (children != null && children.isArray() && !children.isEmpty()) {
                out.addAll(flattenMaterialGroups(children));
            }
        }
        return out;
    }

    private int[] upsertMaterials(JsonNode envelope) {
        JsonNode list = envelope.path("payload").path("data").path("list");
        if (!list.isArray()) {
            throw new ServiceException("material list 缺失");
        }
        Integer envelopeType = intOrNull(envelope.get("material_type"));
        String envelopeKind = text(envelope, "material_kind");
        String envelopeAppId = text(envelope, "app_id");
        int inserted = 0;
        int updated = 0;
        for (JsonNode item : list) {
            String materialId = text(item, "material_id");
            if (!StringUtils.hasText(materialId)) {
                continue;
            }
            XiaoeMaterials existing = xiaoeMaterialsService.queryChain()
                    .eq(XiaoeMaterials::getMaterialId, materialId)
                    .one();
            XiaoeMaterials row = existing != null ? existing : new XiaoeMaterials();
            fillMaterialRow(row, item, envelopeType, envelopeKind, envelopeAppId);
            if (existing == null) {
                xiaoeMaterialsService.save(row);
                inserted++;
            } else {
                xiaoeMaterialsService.updateById(row);
                updated++;
            }
        }
        return new int[] { inserted, updated };
    }

    private void fillMaterialRow(XiaoeMaterials row, JsonNode item, Integer envelopeType,
            String envelopeKind, String envelopeAppId) {
        Integer type = intOrNull(item.get("type"));
        if (type == null) {
            type = envelopeType;
        }
        row.setMaterialId(text(item, "material_id"));
        row.setMaterialRecId(longOrNull(item.get("id")));
        row.setAppId(StringUtils.hasText(envelopeAppId) ? envelopeAppId : text(item, "app_id"));
        row.setMaterialType(type);
        row.setMaterialKind(resolveMaterialKind(envelopeKind, type));
        row.setSubType(intOrNull(item.get("sub_type")));
        row.setTitle(text(item, "title"));

        String url = text(item, "url");
        String showUrl = text(item, "show_url");
        String downloadUrl = text(item, "download_url");
        row.setUrl(url);
        row.setShowUrl(showUrl);
        row.setDownloadUrl(downloadUrl);
        // 桌面下载器 token 非 HTTP；原始链接优先 download_url，否则列表 url（CDN/VOD 源）
        String original = StringUtils.hasText(downloadUrl) ? downloadUrl : url;
        row.setOriginalUrl(original);

        row.setCategoryId(longOrNull(item.get("category_id")));
        row.setCategoryName(text(item, "category_name"));
        row.setCreatorId(text(item, "creator_id"));
        row.setCreatorName(text(item, "creator_name"));
        row.setMaterialSize(text(item, "material_size"));

        JsonNode prop = item.get("material_property");
        if (prop != null && prop.isTextual()) {
            try {
                prop = objectMapper.readTree(prop.asText());
            } catch (Exception e) {
                prop = null;
            }
        }
        if (prop != null && prop.isObject()) {
            row.setFileId(text(prop, "file_id"));
            row.setWidth(intOrNull(prop.get("width")));
            row.setHeight(intOrNull(prop.get("height")));
            if (prop.has("length") && !prop.get("length").isNull()) {
                try {
                    row.setLengthSec(BigDecimal.valueOf(prop.get("length").asDouble())
                            .setScale(4, RoundingMode.HALF_UP));
                } catch (Exception e) {
                    row.setLengthSec(null);
                }
            } else {
                row.setLengthSec(null);
            }
            row.setPixelData(text(prop, "pixel_data"));
            row.setPatchImgUrl(text(prop, "patch_img_url"));
            row.setMaterialProperty(toJson(prop));
        } else {
            row.setFileId(null);
            row.setWidth(null);
            row.setHeight(null);
            row.setLengthSec(null);
            row.setPixelData(null);
            row.setPatchImgUrl(null);
            row.setMaterialProperty(null);
        }

        row.setState(intOrNull(item.get("state")));
        row.setAuditState(intOrNull(item.get("audit_state")));
        row.setBannedState(intOrNull(item.get("banned_state")));
        row.setMaterialState(intOrNull(item.get("material_state")));
        row.setMaterialStatus(intOrNull(item.get("material_status")));
        row.setDealState(intOrNull(item.get("deal_state")));
        row.setDecodeState(intOrNull(item.get("decode_state")));
        row.setDecodeDescription(text(item, "decode_description"));
        row.setReauditStatus(intOrNull(item.get("reaudit_status")));
        JsonNode prot = item.get("protection_status");
        if (prot != null && prot.isBoolean()) {
            row.setProtectionStatus(prot.asBoolean() ? 1 : 0);
        } else {
            row.setProtectionStatus(intOrNull(prot));
        }
        row.setMaterialSource(intOrNull(item.get("material_source")));
        row.setCostSummary(intOrNull(item.get("cost_summary")));
        row.setReferCount(intOrNull(item.get("refer_count")));
        row.setViewCount(intOrNull(item.get("view_count")));
        row.setBannedReason(text(item, "banned_reason"));
        row.setBannedAt(parseFlexibleTime(item.get("banned_at")));
        row.setLatestViewAt(parseFlexibleTime(item.get("latest_view_at")));
        row.setMaterialCreatedAt(parseFlexibleTime(item.get("created_at")));
        row.setMaterialUpdatedAt(parseFlexibleTime(item.get("updated_at")));
        row.setExtendData(toJson(item.get("extend_data")));
        row.setJsonData(toJson(item));
        row.setSyncStatus("SYNCED");
        row.setLastSyncTime(Instant.now());
        row.setDelFlag(0);
        if (!StringUtils.hasText(row.getStatus())) {
            row.setStatus("0");
        }
    }

    private static String resolveMaterialKind(String envelopeKind, Integer materialType) {
        if (StringUtils.hasText(envelopeKind)) {
            return envelopeKind.trim().toUpperCase();
        }
        if (materialType == null) {
            return null;
        }
        return switch (materialType) {
            case 1 -> "IMAGE";
            case 2 -> "AUDIO";
            case 3 -> "VIDEO";
            default -> "MT_" + materialType;
        };
    }

    private int[] upsertSvipMembers(JsonNode envelope) {
        JsonNode list = envelope.path("payload").path("data").path("list");
        if (!list.isArray()) {
            throw new ServiceException("svip member list 缺失");
        }
        JsonNode meta = envelope.path("svip_meta");
        if (meta.isMissingNode() || meta.isNull()) {
            meta = envelope.path("payload").path("svip_meta");
        }
        JsonNode phones = envelope.path("payload").path("phones");
        JsonNode phonesBound = envelope.path("payload").path("phones_bound");
        JsonNode phonesCollect = envelope.path("payload").path("phones_collect");
        int inserted = 0;
        int updated = 0;
        for (JsonNode item : list) {
            Long memberRecId = longOrNull(item.get("id"));
            String userId = text(item, "user_id");
            if (memberRecId == null || !StringUtils.hasText(userId)) {
                continue;
            }
            XiaoeSvipMembers existing = xiaoeSvipMembersService.queryChain()
                    .eq(XiaoeSvipMembers::getMemberRecId, memberRecId)
                    .one();
            XiaoeSvipMembers row = existing != null ? existing : new XiaoeSvipMembers();
            fillSvipMemberRow(row, item, envelope, meta, phones, phonesBound, phonesCollect);
            if (existing == null) {
                xiaoeSvipMembersService.save(row);
                inserted++;
            } else {
                xiaoeSvipMembersService.updateById(row);
                updated++;
            }
        }
        return new int[] { inserted, updated };
    }

    private void fillSvipMemberRow(XiaoeSvipMembers row, JsonNode item, JsonNode envelope, JsonNode meta,
                                   JsonNode phones, JsonNode phonesBound, JsonNode phonesCollect) {
        row.setMemberRecId(longOrNull(item.get("id")));
        row.setAppId(firstText(item, "app_id", envelope, "app_id", meta, "app_id"));
        String svipId = firstText(item, "svip_id", envelope, "svip_id", meta, "svip_id");
        row.setSvipId(svipId);
        row.setSvipTitle(firstText(envelope, "svip_title", meta, "title", envelope, "svip_title"));
        if (!StringUtils.hasText(row.getSvipTitle())) {
            row.setSvipTitle(text(meta, "title"));
        }
        row.setSvipSubTitle(firstText(envelope, "svip_sub_title", meta, "sub_title"));
        Integer level = intOrNull(envelope.get("svip_level"));
        if (level == null) {
            level = intOrNull(meta.get("svip_level"));
        }
        row.setSvipLevel(level);
        Integer svipState = intOrNull(envelope.get("svip_state"));
        if (svipState == null) {
            svipState = intOrNull(meta.get("state"));
        }
        row.setSvipState(svipState);
        row.setSvipPageUrl(firstText(envelope, "svip_page_url", meta, "page_url"));

        String specId = text(item, "sepc_id");
        if (!StringUtils.hasText(specId)) {
            specId = text(item, "spec_id");
        }
        if (!StringUtils.hasText(specId) && meta != null) {
            JsonNode specs = meta.get("spec");
            if (specs != null && specs.isArray() && specs.size() > 0) {
                specId = text(specs.get(0), "spec_id");
                if (!StringUtils.hasText(specId)) {
                    specId = asState(specs.get(0).get("spec_id"));
                }
            }
        }
        row.setSpecId(specId);
        BigDecimal specPrice = centsToYuan(envelope.get("spec_price_cents"));
        if (specPrice == null) {
            specPrice = centsToYuan(meta.path("spec0_price"));
        }
        if (specPrice == null && meta != null && meta.has("price")) {
            specPrice = centsToYuan(meta.get("price"));
        }
        // 油猴可直接传元
        if (specPrice == null) {
            JsonNode yuan = envelope.get("spec_price");
            if (yuan != null && yuan.isNumber()) {
                specPrice = BigDecimal.valueOf(yuan.asDouble()).setScale(4, RoundingMode.HALF_UP);
            }
        }
        row.setSpecPrice(specPrice);
        row.setSpecPeriod(intOrNull(envelope.get("spec_period")));
        row.setSpecUnit(intOrNull(envelope.get("spec_unit")));

        row.setUserId(text(item, "user_id"));
        row.setUnionId(text(item, "union_id"));
        row.setIdentityType(intOrNull(item.get("identity_type")));
        row.setStartTime(parseLocalDate(item.get("start_time")));
        row.setEndTime(parseLocalDate(item.get("end_time")));
        row.setIsForever(intOrNull(item.get("is_forever")));
        row.setMemberState(intOrNull(item.get("state")));
        row.setExpirationDays(intOrNull(item.get("expiration_days")));
        row.setNickName(text(item, "nick_name"));
        row.setRealName(text(item, "real_name"));
        row.setWxAvatar(text(item, "wx_avatar"));
        applySvipPhoneFields(row, text(item, "user_id"), item, phones, phonesBound, phonesCollect);
        row.setIsSeal(intOrNull(item.get("is_seal")));
        row.setBirth(text(item, "birth"));
        row.setAge(intOrNull(item.get("age")));
        row.setWxGender(intOrNull(item.get("wx_gender")));
        row.setIndustry(text(item, "industry"));
        row.setCompany(text(item, "company"));
        row.setJob(text(item, "job"));
        row.setArea(text(item, "area"));
        row.setAddress(text(item, "address"));
        row.setUserFrom(text(item, "user_from"));
        row.setBelongPromoter(text(item, "belong_promoter"));
        row.setIsWeworkCustomer(intOrNull(item.get("is_wework_customer")));
        row.setBuyTimes(intOrNull(item.get("buy_times")));
        row.setPayMoney(centsToYuan(item.get("pay_money")));
        row.setFirstPayTime(parseFlexibleTime(item.get("first_pay_time")));
        row.setLastestPayTime(parseFlexibleTime(item.get("lastest_pay_time")));
        row.setLatestVisitedAt(parseFlexibleTime(item.get("latest_visited_at")));
        row.setUserCreatedAt(parseFlexibleTime(item.get("user_created_at")));
        row.setUserTags(toJson(item.get("user_tags")));
        row.setCorpTags(toJson(item.get("corp_tags")));
        row.setFollowUsers(toJson(item.get("follow_users")));

        ObjectNode snap = objectMapper.createObjectNode();
        snap.set("member", item);
        if (meta != null && !meta.isMissingNode() && !meta.isNull()) {
            snap.set("svip_meta", meta);
        }
        row.setJsonData(toJson(snap));
        row.setSyncStatus("SYNCED");
        row.setLastSyncTime(Instant.now());
        row.setDelFlag(0);
        if (!StringUtils.hasText(row.getStatus())) {
            row.setStatus("0");
        }
    }

    private static String firstText(JsonNode a, String af, JsonNode b, String bf) {
        String v = text(a, af);
        return StringUtils.hasText(v) ? v : text(b, bf);
    }

    private static String firstText(JsonNode a, String af, JsonNode b, String bf, JsonNode c, String cf) {
        String v = firstText(a, af, b, bf);
        return StringUtils.hasText(v) ? v : text(c, cf);
    }

    private static LocalDate parseLocalDate(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        String raw = node.asText();
        if (!StringUtils.hasText(raw)) {
            return null;
        }
        String s = raw.trim();
        try {
            if (s.length() >= 10) {
                return LocalDate.parse(s.substring(0, 10), XE_DATE);
            }
            return LocalDate.parse(s, XE_DATE);
        } catch (Exception e) {
            return null;
        }
    }

    private static Long longOrNull(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        if (node.isNumber()) {
            return node.asLong();
        }
        String s = node.asText();
        if (!StringUtils.hasText(s)) {
            return null;
        }
        try {
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return null;
        }
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
            row.setPayTypeCode(asState(item.get("pay_type")));
            row.setPayType(text(item, "pay_type_description"));
            if (!StringUtils.hasText(row.getPayType())) {
                row.setPayType(row.getPayTypeCode());
            }
            row.setOrderType(asState(item.get("order_type")));
            row.setOrderTypeDesc(text(item, "order_type_description"));
            row.setShipWay(asState(item.get("ship_way_choose_type")));
            row.setShipWayDesc(text(item, "ship_way_choose_type_description"));
            row.setChannelSource(text(item, "channel_source_description"));
            row.setAppId(text(item, "app_id"));
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
            row.setXiaoeUserId(xeUserId);
            row.setXiaoeNickName(text(item, "nick_name"));
            String buyerPhone = resolveBuyerPhone(item);
            XiaoeUserMapping mapping = null;
            if (StringUtils.hasText(xeUserId)) {
                mapping = xiaoeUserMappingService.queryChain()
                        .eq(XiaoeUserMapping::getXiaoeUserId, xeUserId)
                        .one();
                if (mapping != null && mapping.getUserId() != null) {
                    row.setUserId(mapping.getUserId());
                }
                if (!isValidPhone(buyerPhone) && mapping != null && isValidPhone(mapping.getPhone())) {
                    buyerPhone = mapping.getPhone().trim();
                }
                if (!StringUtils.hasText(row.getXiaoeNickName()) && mapping != null) {
                    row.setXiaoeNickName(mapping.getNickName());
                }
            }
            row.setBuyerPhone(buyerPhone);
            if (isValidPhone(buyerPhone)) {
                row.setClaimPhone(buyerPhone);
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
        JsonNode phonesBound = envelope.path("payload").path("phones_bound");
        JsonNode phonesCollect = envelope.path("payload").path("phones_collect");
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
            String bound = phoneFromMap(phonesBound, xeId);
            String collect = phoneFromMap(phonesCollect, xeId);
            // 兼容旧脚本只上报 phones
            String preferred = phoneFromMap(phones, xeId);
            if (!isValidPhone(bound) && isValidPhone(preferred)) {
                bound = preferred;
            }
            if (!isValidPhone(collect) && isValidPhone(preferred) && !preferred.equals(bound)) {
                collect = preferred;
            }
            String phone = isValidPhone(bound) ? bound : (isValidPhone(collect) ? collect : null);

            String nick = text(item, "user_name");
            if (!StringUtils.hasText(nick)) {
                nick = text(item, "comment_name");
            }

            ObjectNode snap = objectMapper.createObjectNode();
            snap.set("list_item", item);
            if (phone != null) {
                snap.put("phone", phone);
            } else {
                snap.putNull("phone");
            }
            if (isValidPhone(bound)) {
                snap.put("phone_bound", bound);
            }
            if (isValidPhone(collect)) {
                snap.put("phone_collect", collect);
            }
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
                applyPhoneOnWrite(row, phone, collect, true, retryAt);
                xiaoeUserMappingService.save(row);
                inserted++;
            } else {
                existing.setNickName(nick);
                existing.setJsonData(jsonData);
                applyPhoneOnWrite(existing, phone, collect, false, retryAt);
                xiaoeUserMappingService.updateById(existing);
                // updateById 默认忽略 null：有效号时必须显式清空 phone_next_query_at；
                // 缺号时显式把 phone 置 null（避免库里残留 ""）
                if (isValidPhone(phone)) {
                    UpdateChain.of(XiaoeUserMapping.class)
                            .set(XiaoeUserMapping::getPhoneNextQueryAt, null)
                            .eq(XiaoeUserMapping::getId, existing.getId())
                            .update();
                } else if (!isValidPhone(existing.getPhone())) {
                    UpdateChain.of(XiaoeUserMapping.class)
                            .set(XiaoeUserMapping::getPhone, null)
                            .set(XiaoeUserMapping::getPhoneNextQueryAt, existing.getPhoneNextQueryAt())
                            .eq(XiaoeUserMapping::getId, existing.getId())
                            .update();
                }
                maybeWriteSysUserPhone(existing, phone);
                updated++;
            }
        }
        return new int[] { inserted, updated };
    }

    /**
     * phone：主号（绑定优先，否则采集）；collectionPhone：最近采集明文。
     * 空串/非法号不覆盖已有有效号，仍缺号则 +N 天。
     */
    private void applyPhoneOnWrite(XiaoeUserMapping row, String phone, String collectionPhone,
                                   boolean isNew, Instant retryAt) {
        if (isValidPhone(collectionPhone)) {
            row.setCollectionPhone(collectionPhone);
        } else if (!isValidPhone(row.getCollectionPhone())) {
            row.setCollectionPhone(null);
        }
        if (isValidPhone(phone)) {
            row.setPhone(phone);
            row.setPhoneNextQueryAt(null);
            return;
        }
        if (isValidPhone(row.getPhone())) {
            return;
        }
        row.setPhone(null);
        row.setPhoneNextQueryAt(retryAt);
        if (isNew) {
            log.debug("xeknow new user missing phone, nextQueryAt={}", retryAt);
        }
    }

    /** 超会列表常带掩码：仅明文写入；有 map 明文优先；无明文不清掉已有有效号 */
    private void applySvipPhoneFields(XiaoeSvipMembers row, String userId, JsonNode item,
                                      JsonNode phones, JsonNode phonesBound, JsonNode phonesCollect) {
        String bound = firstValidPhone(
                phoneFromMap(phonesBound, userId),
                phoneFromMap(phones, userId),
                normalizePhone(text(item, "phone_number")));
        String collect = firstValidPhone(
                phoneFromMap(phonesCollect, userId),
                normalizePhone(text(item, "collection_phone")));
        if (isValidPhone(bound)) {
            row.setPhoneNumber(bound);
        } else if (!isValidPhone(row.getPhoneNumber())) {
            row.setPhoneNumber(null);
        }
        if (isValidPhone(collect)) {
            row.setCollectionPhone(collect);
        } else if (!isValidPhone(row.getCollectionPhone())) {
            row.setCollectionPhone(null);
        }
    }

    private static String phoneFromMap(JsonNode map, String userId) {
        if (map == null || map.isMissingNode() || map.isNull() || !StringUtils.hasText(userId)) {
            return null;
        }
        if (!map.has(userId) || map.get(userId).isNull()) {
            return null;
        }
        return normalizePhone(map.get(userId).asText(null));
    }

    private static String firstValidPhone(String... candidates) {
        if (candidates == null) {
            return null;
        }
        for (String c : candidates) {
            if (isValidPhone(c)) {
                return c.trim();
            }
        }
        return null;
    }

    private void maybeWriteSysUserPhone(XiaoeUserMapping mapping, String phone) {
        if (!isValidPhone(phone) || mapping.getUserId() == null) {
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
        row.setUnitPrice(centsToYuan(g0.get("goods_original_unit_price")));
        if (g0.has("goods_buy_num") && g0.get("goods_buy_num").isNumber()) {
            row.setQuantity(g0.get("goods_buy_num").asInt());
        }
        // 订单级无状态描述时，用商品订单状态描述兜底
        if (!StringUtils.hasText(row.getOrderStateDesc())) {
            row.setOrderStateDesc(text(g0, "goods_order_state_description"));
        }
    }

    /** 手机号优先级：学员信息 > 收货人 > 无效则 null */
    private String resolveBuyerPhone(JsonNode item) {
        String fromStudent = null;
        JsonNode stu = item.get("student_info");
        if (stu != null && !stu.isNull()) {
            fromStudent = text(stu, "phone");
        }
        if (isValidPhone(fromStudent)) {
            return fromStudent.trim();
        }
        String consignee = text(item, "consignee_phone");
        if (isValidPhone(consignee)) {
            return consignee.trim();
        }
        return null;
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

    /** 支持秒级时间戳、毫秒时间戳、yyyy-MM-dd HH:mm:ss、ISO */
    private static Instant parseFlexibleTime(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        if (node.isNumber()) {
            long v = node.asLong();
            if (v <= 0) {
                return null;
            }
            // 10 位秒 / 13 位毫秒
            return v < 1_000_000_000_000L
                    ? Instant.ofEpochSecond(v)
                    : Instant.ofEpochMilli(v);
        }
        String raw = node.asText();
        if (!StringUtils.hasText(raw) || "0".equals(raw.trim())) {
            return null;
        }
        String s = raw.trim();
        if (s.matches("\\d{10,13}")) {
            long v = Long.parseLong(s);
            return v < 1_000_000_000_000L
                    ? Instant.ofEpochSecond(v)
                    : Instant.ofEpochMilli(v);
        }
        return parseXeTime(s);
    }

    private static Integer intOrNull(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        if (node.isNumber()) {
            return node.asInt();
        }
        String s = node.asText();
        if (!StringUtils.hasText(s)) {
            return null;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
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

    /** 归一化：去空白；空串/仅星号掩码等返回 null */
    private static String normalizePhone(String raw) {
        if (raw == null) {
            return null;
        }
        String p = raw.trim();
        if (p.isEmpty() || p.contains("*")) {
            return null;
        }
        return p;
    }

    /** 仅大陆 11 位手机号视为有效，其余（含 ""）一律缺号待重查 */
    private static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        String p = phone.trim();
        return p.matches("1[3-9]\\d{9}");
    }
}
