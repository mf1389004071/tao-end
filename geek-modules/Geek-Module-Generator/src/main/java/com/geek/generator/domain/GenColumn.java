package com.geek.generator.domain;

import com.geek.common.core.domain.BaseEntity;
import com.geek.common.utils.StringUtils;
import com.geek.generator.constant.GenConstants;
import com.geek.generator.util.GenUtils;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代码生成业务字段表 gen_table_column
 * 
 * @author geek
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenColumn extends BaseEntity {

    /** 编号 */
    private Long columnId;

    /** 归属表编号 */
    private Long tableId;

    /** 列名称 */
    private String columnName;

    /** 列描述 */
    private String columnComment;

    /** 列类型 */
    private String columnType;

    /** JAVA类型 */
    private String javaType;

    /** JAVA字段名 */
    @NotBlank(message = "Java属性不能为空")
    private String javaField;

    /** 是否主键（1是） */
    private String isPk;

    /** 是否自增（1是） */
    private String isIncrement;

    /** 是否必填（1是） */
    private String isRequired;

    /** 是否为插入字段（1是） */
    private String isInsert;

    /** 是否编辑字段（1是） */
    private String isEdit;

    /** 是否列表字段（1是） */
    private String isList;

    /** 是否查询字段（1是） */
    private String isQuery;

    /** 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围） */
    private String queryType;

    /**
     * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件）
     */
    private String htmlType;

    /** 字典类型 */
    private String dictType;

    /** 排序 */
    private Integer sort;

    /** 关联表名称 */
    @Deprecated
    private String subColumnTableName;

    /** 关联字段名称 */
    @Deprecated
    private String subColumnFkName;

    /** 映射字段名称 */
    @Deprecated
    private String subColumnName;

    /** 映射字段Java字段名 */
    @Deprecated
    private String subColumnJavaField;

    /** 映射字段Java类型 */
    @Deprecated
    private String subColumnJavaType;

    public String getCapJavaField() {
        return StringUtils.capitalize(javaField);
    }

    public boolean isPk() {
        return isPk(this.isPk);
    }

    public boolean isPk(String isPk) {
        return isPk != null && StringUtils.equals("1", isPk);
    }

    public boolean isIncrement() {
        return isIncrement(this.isIncrement);
    }

    public boolean isIncrement(String isIncrement) {
        return isIncrement != null && StringUtils.equals("1", isIncrement);
    }

    public boolean isRequired() {
        return isRequired(this.isRequired);
    }

    public boolean isRequired(String isRequired) {
        return isRequired != null && StringUtils.equals("1", isRequired);
    }

    public boolean isInsert() {
        return isInsert(this.isInsert);
    }

    public boolean isInsert(String isInsert) {
        return isInsert != null && StringUtils.equals("1", isInsert);
    }

    public boolean isEdit() {
        return isInsert(this.isEdit);
    }

    public boolean isEdit(String isEdit) {
        return isEdit != null && StringUtils.equals("1", isEdit);
    }

    public boolean isList() {
        return isList(this.isList);
    }

    public boolean isList(String isList) {
        return isList != null && StringUtils.equals("1", isList);
    }

    public boolean isQuery() {
        return isQuery(this.isQuery);
    }

    public boolean isQuery(String isQuery) {
        return isQuery != null && StringUtils.equals("1", isQuery);
    }

    public boolean isSuperColumn() {
        return isSuperColumn(this.javaField);
    }

    public static boolean isSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField,
                // BaseEntity
                "createBy", "createTime", "updateBy", "updateTime", "remark",
                // TreeEntity
                "parentName", "parentId", "orderNum", "ancestors");
    }

    public boolean isUsableColumn() {
        return isUsableColumn(javaField);
    }

    public static boolean isUsableColumn(String javaField) {
        // isSuperColumn()中的名单用于避免生成多余Domain属性，若某些属性在生成页面时需要用到不能忽略，则放在此处白名单
        return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "orderNum", "remark");
    }

    /**
     * 生成实体字段时的 Java 类型（日期/时间统一为 Instant；整型按数据库类型精确映射，避免 bigint→String）
     */
    public String getJavaTypeForField() {
        if (isDateOrTimeType()) {
            return "Instant";
        }
        // 防御：若配置中 javaType 为 String 但列类型为数字/时间，按 columnType 推断正确类型
        if (StringUtils.isNotEmpty(columnType) && GenConstants.TYPE_STRING.equals(javaType)) {
            String inferred = inferJavaTypeFromColumnType(columnType);
            if (inferred != null) {
                return inferred;
            }
        }
        return javaType;
    }

    /**
     * 根据数据库列类型推断 Java 类型（用于修正历史错误配置，如 bigint 被存成 String）
     */
    private static String inferJavaTypeFromColumnType(String columnType) {
        String dataType = GenUtils.getDbType(columnType);
        if (dataType == null) {
            return null;
        }
        String lower = dataType.toLowerCase();
        if ("bigint".equals(lower) || "int8".equals(lower) || "bigserial".equals(lower)) {
            return "Long";
        }
        if ("int".equals(lower) || "integer".equals(lower) || "smallint".equals(lower)
                || "mediumint".equals(lower) || "tinyint".equals(lower)
                || "int4".equals(lower) || "int2".equals(lower) || "serial".equals(lower) || "smallserial".equals(lower)) {
            return "Integer";
        }
        if ("datetime".equals(lower) || "timestamp".equals(lower) || "timestamptz".equals(lower)
                || "date".equals(lower) || "time".equals(lower)) {
            return "Instant";
        }
        if ("decimal".equals(lower) || "numeric".equals(lower)) {
            return "BigDecimal";
        }
        if ("float".equals(lower) || "double".equals(lower)) {
            return "Double";
        }
        if ("bit".equals(lower) || "bool".equals(lower) || "boolean".equals(lower)) {
            return "Boolean";
        }
        return null;
    }

    /**
     * 日期/时间字段的 JSON 序列化格式（Instant 默认 ISO-8601，可返回 null 使用默认）
     *
     * @return 非日期字段返回 null；Instant 使用默认序列化
     */
    public String getDateFormatPattern() {
        if (!isDateOrTimeType()) {
            return null;
        }
        return null;
    }

    /** 是否为日期/时间类型（实体中统一为 java.time.Instant，含旧配置 Date/Local*） */
    public boolean isDateOrTimeType() {
        return GenConstants.TYPE_DATE.equals(javaType)
                || GenConstants.TYPE_TIME.equals(javaType)
                || GenConstants.TYPE_DATETIME.equals(javaType)
                || "Date".equals(javaType)
                || "LocalDate".equals(javaType)
                || "LocalTime".equals(javaType)
                || "LocalDateTime".equals(javaType);
    }

    public String readConverterExp() {
        String remarks = StringUtils.substringBetween(this.columnComment, "（", "）");
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(remarks)) {
            for (String value : remarks.split(" ")) {
                if (StringUtils.isNotEmpty(value)) {
                    Object startStr = value.subSequence(0, 1);
                    String endStr = value.substring(1);
                    sb.append(startStr).append("=").append(endStr).append(",");
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return this.columnComment;
        }
    }
}
