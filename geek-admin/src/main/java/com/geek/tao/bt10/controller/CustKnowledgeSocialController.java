package com.geek.tao.bt10.controller;

import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.page.PageDomain;
import com.geek.common.core.page.TableDataInfo;
import com.geek.common.core.page.TableSupport;
import com.geek.common.utils.SecurityUtils;
import com.geek.tao.bt10.domain.KnowledgeAction;
import com.geek.tao.bt10.domain.KnowledgeComment;
import com.geek.tao.bt10.domain.KnowledgeContent;
import com.geek.tao.bt10.domain.Notifications;
import com.geek.tao.bt10.mapper.KnowledgeActionMapper;
import com.geek.tao.bt10.mapper.KnowledgeCommentMapper;
import com.geek.tao.bt10.mapper.KnowledgeContentMapper;
import com.geek.tao.bt10.mapper.NotificationsMapper;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * C 端（小程序）知识内容社交动作：浏览/点赞/收藏/分享/评论
 *
 * 说明：
 * - 复用 bt10 已建表：knowledge_content / knowledge_action / knowledge_comment
 * - 不走后台权限点（PreAuthorize），仅要求用户登录（SecurityContext 有用户）
 */
@RestController
@RequestMapping("/bt10/cust/knowledge")
public class CustKnowledgeSocialController extends BaseController {

    @Autowired
    private KnowledgeContentMapper knowledgeContentMapper;
    @Autowired
    private KnowledgeActionMapper knowledgeActionMapper;
    @Autowired
    private KnowledgeCommentMapper knowledgeCommentMapper;
    @Autowired
    private NotificationsMapper notificationsMapper;

    /** 按「内容+用户+动作」串行化点赞/收藏，避免并发重复累加 */
    private final ConcurrentHashMap<String, Object> actionLocks = new ConcurrentHashMap<>();

    private Object lockForAction(Long contentId, Long uid, String actionType) {
        String key = contentId + ":" + uid + ":" + actionType;
        return actionLocks.computeIfAbsent(key, k -> new Object());
    }

    public static class ToggleActionReq {
        public String contentId;
        public String actionType; // LIKE/COLLECT
        public Boolean enabled;   // 兼容旧参数，toggle 接口不再信任该值
    }

    public static class AddCommentReq {
        public String contentId;
        public String parentId;
        public String content;
    }

    private Long requireLoginUserId() {
        if (SecurityUtils.isAnonymous()) {
            return null;
        }
        return SecurityUtils.getUserId();
    }

    private static Long parseLongOrNull(String v) {
        if (v == null) return null;
        String s = v.trim();
        if (s.isEmpty()) return null;
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static String normActionType(String v) {
        if (v == null) return "";
        return v.trim().toUpperCase();
    }

    /** 含已逻辑删除行：全局 del_flag 导致软删后唯一索引仍存在，普通查询查不到，INSERT 会冲突 */
    private KnowledgeAction selectActionIncludingDeleted(Long contentId, Long uid, String actionType) {
        QueryWrapper qw = QueryWrapper.create()
            .from(KnowledgeAction.class)
            .eq(KnowledgeAction::getContentId, contentId)
            .eq(KnowledgeAction::getUserId, uid)
            .eq(KnowledgeAction::getActionType, actionType);
        return LogicDeleteManager.execWithoutLogicDelete(() -> knowledgeActionMapper.selectOneByQuery(qw));
    }

    private void restoreActionIfSoftDeleted(KnowledgeAction row) {
        if (row == null) return;
        Integer df = row.getDelFlag();
        if (df != null && df != 0) {
            row.setDelFlag(0);
            knowledgeActionMapper.update(row);
        }
    }

    @PostMapping("/share")
    @Transactional
    public AjaxResult addShare(@RequestBody Map<String, Object> body) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");

        Object cidObj = body == null ? null : body.get("contentId");
        Long contentId = parseLongOrNull(cidObj == null ? null : String.valueOf(cidObj));
        if (contentId == null) return error("contentId 不能为空");

        KnowledgeContent kc = knowledgeContentMapper.selectOneById(contentId);
        if (kc == null) return error("内容不存在");

        // 分享计数：按点击次数 +1（MVP），可后续接入真实分享回调/风控
        kc.setShareCount((kc.getShareCount() == null ? 0L : kc.getShareCount()) + 1);
        knowledgeContentMapper.update(kc);

        // 行为记录：每用户每内容仅记录一次 SHARE
        QueryWrapper qw = QueryWrapper.create()
            .from(KnowledgeAction.class)
            .eq(KnowledgeAction::getContentId, contentId)
            .eq(KnowledgeAction::getUserId, uid)
            .eq(KnowledgeAction::getActionType, "SHARE");
        KnowledgeAction exists = knowledgeActionMapper.selectOneByQuery(qw);
        if (exists == null) {
            KnowledgeAction dead = selectActionIncludingDeleted(contentId, uid, "SHARE");
            if (dead != null) {
                restoreActionIfSoftDeleted(dead);
            } else {
                KnowledgeAction a = new KnowledgeAction();
                a.setContentId(contentId);
                a.setUserId(uid);
                a.setActionType("SHARE");
                knowledgeActionMapper.insert(a);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("shareCount", kc.getShareCount());
        return success(data);
    }

    @PostMapping("/view")
    @Transactional
    public AjaxResult addView(@RequestBody Map<String, Object> body) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");

        Object cidObj = body == null ? null : body.get("contentId");
        Long contentId = parseLongOrNull(cidObj == null ? null : String.valueOf(cidObj));
        if (contentId == null) return error("contentId 不能为空");

        KnowledgeContent kc = knowledgeContentMapper.selectOneById(contentId);
        if (kc == null) return error("内容不存在");

        // 计数：简单 +1（MVP），后续可按 UV/时间窗去重
        kc.setViewCount((kc.getViewCount() == null ? 0L : kc.getViewCount()) + 1);
        knowledgeContentMapper.update(kc);

        // 行为记录：用 knowledge_action 记一次（每用户每内容只记录一次 VIEW）
        QueryWrapper qw = QueryWrapper.create()
            .from(KnowledgeAction.class)
            .eq(KnowledgeAction::getContentId, contentId)
            .eq(KnowledgeAction::getUserId, uid)
            .eq(KnowledgeAction::getActionType, "VIEW");
        KnowledgeAction exists = knowledgeActionMapper.selectOneByQuery(qw);
        if (exists == null) {
            KnowledgeAction dead = selectActionIncludingDeleted(contentId, uid, "VIEW");
            if (dead != null) {
                restoreActionIfSoftDeleted(dead);
            } else {
                KnowledgeAction a = new KnowledgeAction();
                a.setContentId(contentId);
                a.setUserId(uid);
                a.setActionType("VIEW");
                knowledgeActionMapper.insert(a);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("viewCount", kc.getViewCount());
        return success(data);
    }

    @PostMapping("/action/toggle")
    @Transactional
    public AjaxResult toggleAction(@RequestBody ToggleActionReq req) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");

        Long contentId = parseLongOrNull(req == null ? null : req.contentId);
        if (contentId == null) return error("contentId 不能为空");
        String actionType = normActionType(req.actionType);
        if (!("LIKE".equals(actionType) || "COLLECT".equals(actionType))) {
            return error("actionType 不合法");
        }

        synchronized (lockForAction(contentId, uid, actionType)) {
            KnowledgeContent kc = knowledgeContentMapper.selectOneById(contentId);
            if (kc == null) return error("内容不存在");

            QueryWrapper qw = QueryWrapper.create()
                .from(KnowledgeAction.class)
                .eq(KnowledgeAction::getContentId, contentId)
                .eq(KnowledgeAction::getUserId, uid)
                .eq(KnowledgeAction::getActionType, actionType);
            KnowledgeAction exists = knowledgeActionMapper.selectOneByQuery(qw);

            // 连点场景统一以数据库当前状态反转，避免前端旧状态导致重复 insert
            boolean nextEnabled = (exists == null);

            if (nextEnabled && exists == null) {
                KnowledgeAction dead = selectActionIncludingDeleted(contentId, uid, actionType);
                if (dead != null) {
                    restoreActionIfSoftDeleted(dead);
                    exists = knowledgeActionMapper.selectOneByQuery(qw);
                    if (exists != null) {
                        bumpCount(kc, actionType, +1);
                        knowledgeContentMapper.update(kc);
                    }
                }
                if (exists == null) {
                    KnowledgeAction a = new KnowledgeAction();
                    a.setContentId(contentId);
                    a.setUserId(uid);
                    a.setActionType(actionType);
                    try {
                        knowledgeActionMapper.insert(a);
                        bumpCount(kc, actionType, +1);
                        knowledgeContentMapper.update(kc);
                    } catch (DuplicateKeyException e) {
                        // 并发插入时唯一键冲突：按已存在处理，避免 500
                        exists = knowledgeActionMapper.selectOneByQuery(qw);
                        if (exists == null) {
                            KnowledgeAction conflictRow = selectActionIncludingDeleted(contentId, uid, actionType);
                            restoreActionIfSoftDeleted(conflictRow);
                        }
                    }
                }
            } else if (!nextEnabled && exists != null) {
                knowledgeActionMapper.deleteByQuery(qw);
                bumpCount(kc, actionType, -1);
                knowledgeContentMapper.update(kc);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("enabled", nextEnabled);
            data.put("likeCount", kc.getLikeCount());
            data.put("collectCount", kc.getCollectCount());
            data.put("shareCount", kc.getShareCount());
            return success(data);
        }
    }

    /** 当前用户对内容的点赞/收藏状态（用于详情页回显，避免重复累加误判） */
    @GetMapping("/action/status")
    public AjaxResult actionStatus(String contentId) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");
        Long cid = parseLongOrNull(contentId);
        if (cid == null) return error("contentId 不能为空");

        QueryWrapper qwLike = QueryWrapper.create()
            .from(KnowledgeAction.class)
            .eq(KnowledgeAction::getContentId, cid)
            .eq(KnowledgeAction::getUserId, uid)
            .eq(KnowledgeAction::getActionType, "LIKE");
        QueryWrapper qwCol = QueryWrapper.create()
            .from(KnowledgeAction.class)
            .eq(KnowledgeAction::getContentId, cid)
            .eq(KnowledgeAction::getUserId, uid)
            .eq(KnowledgeAction::getActionType, "COLLECT");
        boolean liked = knowledgeActionMapper.selectOneByQuery(qwLike) != null;
        boolean collected = knowledgeActionMapper.selectOneByQuery(qwCol) != null;
        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("collected", collected);
        return success(data);
    }

    private static void bumpCount(KnowledgeContent kc, String actionType, int delta) {
        if ("LIKE".equals(actionType)) {
            long v = (kc.getLikeCount() == null ? 0L : kc.getLikeCount()) + delta;
            kc.setLikeCount(Math.max(0, v));
        } else if ("COLLECT".equals(actionType)) {
            long v = (kc.getCollectCount() == null ? 0L : kc.getCollectCount()) + delta;
            kc.setCollectCount(Math.max(0, v));
        } else if ("SHARE".equals(actionType)) {
            long v = (kc.getShareCount() == null ? 0L : kc.getShareCount()) + delta;
            kc.setShareCount(Math.max(0, v));
        }
    }

    @GetMapping("/comment/list")
    public TableDataInfo<KnowledgeComment> listComments(Long contentId) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        QueryWrapper qw = QueryWrapper.create()
            .from(KnowledgeComment.class)
            .eq(KnowledgeComment::getContentId, contentId)
            .orderBy(KnowledgeComment::getCreateTime, false);
        Page<KnowledgeComment> page = knowledgeCommentMapper.paginate(pageDomain.getPageNum(), pageDomain.getPageSize(), qw);
        return getDataTable(page);
    }

    @GetMapping("/comment/{id}")
    public AjaxResult getComment(@PathVariable("id") String id) {
        Long cid = parseLongOrNull(id);
        if (cid == null) return error("id 不能为空");
        KnowledgeComment c = knowledgeCommentMapper.selectOneById(cid);
        if (c == null) return error("评论不存在");
        return success(c);
    }

    @PostMapping("/comment/add")
    @Transactional
    public AjaxResult addComment(@RequestBody AddCommentReq req) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");

        Long contentId = parseLongOrNull(req == null ? null : req.contentId);
        if (contentId == null) return error("contentId 不能为空");
        String text = req.content;
        if (text == null || text.trim().isEmpty()) return error("评论内容不能为空");

        KnowledgeContent kc = knowledgeContentMapper.selectOneById(contentId);
        if (kc == null) return error("内容不存在");

        KnowledgeComment c = new KnowledgeComment();
        c.setContentId(contentId);
        c.setUserId(uid);
        c.setParentId(parseLongOrNull(req.parentId));
        c.setContent(text.trim());
        c.setLikeCount(0);
        c.setIsPinned(false);
        c.setBizStatus("PUBLISHED");
        knowledgeCommentMapper.insert(c);

        kc.setCommentCount((kc.getCommentCount() == null ? 0L : kc.getCommentCount()) + 1);
        knowledgeContentMapper.update(kc);

        notifyCommentInteraction(uid, kc, c);

        Map<String, Object> data = new HashMap<>();
        data.put("commentId", c.getId());
        data.put("commentCount", kc.getCommentCount());
        return success(data);
    }

    private void notifyCommentInteraction(Long actorUid, KnowledgeContent kc, KnowledgeComment c) {
        if (c == null || c.getId() == null) return;
        String preview = c.getContent();
        if (preview != null && preview.length() > 80) {
            preview = preview.substring(0, 80) + "…";
        }
        Long targetUserId = null;
        if (c.getParentId() == null) {
            Long authorId = kc.getAuthorId();
            if (authorId != null && !authorId.equals(actorUid)) {
                targetUserId = authorId;
            }
        } else {
            KnowledgeComment parent = knowledgeCommentMapper.selectOneById(c.getParentId());
            if (parent != null && parent.getUserId() != null && !parent.getUserId().equals(actorUid)) {
                targetUserId = parent.getUserId();
            }
        }
        if (targetUserId == null) return;

        Notifications n = new Notifications();
        n.setUserId(targetUserId);
        n.setTitle(c.getParentId() == null ? "你的内容收到新评论" : "你的评论收到回复");
        n.setContent(preview == null ? "" : preview);
        n.setNotificationType("interaction");
        n.setRelatedType("KNOWLEDGE_COMMENT");
        n.setRelatedId(c.getId());
        n.setIsRead(false);
        n.setReadTime(null);
        n.setStatus("0");
        n.setDelFlag(0);
        n.setCreateTime(Instant.now());
        notificationsMapper.insert(n);
    }
}

