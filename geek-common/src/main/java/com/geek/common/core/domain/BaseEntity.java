package com.geek.common.core.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.geek.common.annotation.Excel;
import com.mybatisflex.annotation.Column;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Entity基类（时间字段为 UTC 时刻，对应 PostgresSQL 数据库 timestamptz）
 *
 * @author geek
 */
@Schema(title = "基类")
@Data
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 创建者ID */
    @Schema(title = "创建者ID")
    @Excel(name = "创建者ID")
    private Long createId;

    /** 创建者 */
    @Schema(title = "创建者")
    private String createBy;

    /** 创建时间（UTC，ISO-8601 序列化） */
    @Schema(title = "创建时间")
    private Instant createTime;

    /** 更新者ID */
    @Schema(title = "更新者ID")
    @Excel(name = "更新者ID")
    private Long updateId;

    /** 更新者 */
    @Schema(title = "更新者")
    private String updateBy;

    /** 更新时间（UTC，ISO-8601 序列化） */
    @Schema(title = "更新时间")
    private Instant updateTime;

    /** 备注 */
    @Schema(title = "备注")
    private String remark;

    /** 请求参数 */
    @Schema(title = "请求参数", example = "{'pageNum': 1, 'pageSize': 10, 'startXXX':'', 'endXXX':''}")
    @Column(ignore = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private HashMap<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}
