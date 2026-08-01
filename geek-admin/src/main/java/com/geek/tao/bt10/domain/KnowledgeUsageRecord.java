package com.geek.tao.bt10.domain;

import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知识使用记录 knowledge_usage_record
 */
@Table("knowledge_usage_record")
@Schema(title = "知识使用记录")
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeUsageRecord extends BaseEntity {

    @Id
    private Long id;
    private Long contentId;
    private String contentType;
    private String toolCode;
    private Long userId;
    private String identityCode;
    private String subjectName;
    /** JSON 字符串 */
    private String answerJson;
    private String scoreSummary;
    private String resultBasic;
    private String resultPro;
    private String resultHuman;
    private Long commentId;
    private Long aiTaskId;
    private Boolean deducted;
    private Boolean usedUnderMembership;
    private Long deleteId;
    private java.time.Instant deleteTime;
    private String status;
    private Integer delFlag;
}
