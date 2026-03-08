package com.geek.generator.util;

import java.util.Arrays;

import org.apache.commons.lang3.RegExUtils;

import com.geek.common.utils.StringUtils;
import com.geek.generator.config.GenConfig;
import com.geek.generator.constant.GenConstants;
import com.geek.generator.domain.GenColumn;
import com.geek.generator.domain.GenTable;

/**
 * 代码生成器 工具类
 * 
 * @author geek
 */
public class GenUtils
{
    /** gen_table 字段最大长度，与表结构一致，避免 PostgreSQL varchar 超长报错 */
    private static final int MAX_FUNCTION_NAME = 200;
    private static final int MAX_FUNCTION_AUTHOR = 200;
    private static final int MAX_MODULE_NAME = 100;
    private static final int MAX_BUSINESS_NAME = 100;
    private static final int MAX_CREATE_BY = 64;

    /**
     * 初始化表信息
     */
    public static void initTable(GenTable genTable, String operName)
    {
        genTable.setClassName(convertClassName(genTable.getTableName()));
        genTable.setPackageName(GenConfig.getPackageName());
        genTable.setModuleName(truncate(getModuleName(GenConfig.getPackageName()), MAX_MODULE_NAME));
        genTable.setBusinessName(truncate(getBusinessName(genTable.getTableName()), MAX_BUSINESS_NAME));
        genTable.setFunctionName(truncate(replaceText(genTable.getTableComment()), MAX_FUNCTION_NAME));
        genTable.setFunctionAuthor(truncate(GenConfig.getAuthor(), MAX_FUNCTION_AUTHOR));
        genTable.setCreateBy(truncate(operName, MAX_CREATE_BY));
    }

    private static String truncate(String value, int maxLen)
    {
        if (value == null || value.length() <= maxLen) {
            return value;
        }
        return value.substring(0, maxLen);
    }

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenColumn column, GenTable table)
    {
        String dataType = getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        column.setTableId(table.getTableId());
        column.setCreateBy(table.getCreateBy());
        // 设置java字段名
        column.setJavaField(StringUtils.toCamelCase(columnName));
        // 设置默认类型
        column.setJavaType(GenConstants.TYPE_STRING);
        column.setQueryType(GenConstants.QUERY_EQ);

        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType))
        {
            // 字符串长度超过500设置为文本域
            Integer columnLength = getColumnLength(column.getColumnType());
            String htmlType = columnLength >= 500 || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
            column.setHtmlType(htmlType);
        }
        else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType))
        {
            // 统一使用 java.util.Date，格式由 getDateFormatPattern() 根据 columnType 推导
            column.setJavaType(GenConstants.TYPE_DATE);
            column.setHtmlType(GenConstants.HTML_DATETIME);
        }
        else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType))
        {
            column.setHtmlType(GenConstants.HTML_INPUT);
            column.setJavaType(resolveJavaTypeForNumber(column.getColumnType(), dataType));
        }

        // 插入字段（默认所有字段都需要插入）
        column.setIsInsert(GenConstants.REQUIRE);

        // 编辑字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !column.isPk())
        {
            column.setIsEdit(GenConstants.REQUIRE);
        }
        // 列表字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !column.isPk())
        {
            column.setIsList(GenConstants.REQUIRE);
        }
        // 查询字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && !column.isPk())
        {
            column.setIsQuery(GenConstants.REQUIRE);
        }

        // 查询字段类型
        if (StringUtils.endsWithIgnoreCase(columnName, "name"))
        {
            column.setQueryType(GenConstants.QUERY_LIKE);
        }
        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(columnName, "status"))
        {
            column.setHtmlType(GenConstants.HTML_RADIO);
        }
        // 类型&性别字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(columnName, "type")
                || StringUtils.endsWithIgnoreCase(columnName, "sex"))
        {
            column.setHtmlType(GenConstants.HTML_SELECT);
        }
        // 图片字段设置图片上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "image"))
        {
            column.setHtmlType(GenConstants.HTML_IMAGE_UPLOAD);
        }
        // 文件字段设置文件上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "file"))
        {
            column.setHtmlType(GenConstants.HTML_FILE_UPLOAD);
        }
        // 内容字段设置富文本控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "content"))
        {
            column.setHtmlType(GenConstants.HTML_EDITOR);
        }
    }

    /**
     * 校验数组是否包含指定值
     * 
     * @param arr 数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue)
    {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取模块名
     * 
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName)
    {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        return StringUtils.substring(packageName, lastIndex + 1, nameLength);
    }

    /**
     * 获取业务名
     * 
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName)
    {
        int lastIndex = tableName.lastIndexOf("_");
        int nameLength = tableName.length();
        return StringUtils.substring(tableName, lastIndex + 1, nameLength);
    }

    /**
     * 表名转换成Java类名
     * 
     * @param tableName 表名称
     * @return 类名
     */
    public static String convertClassName(String tableName)
    {
        boolean autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (autoRemovePre && StringUtils.isNotEmpty(tablePrefix))
        {
            String[] searchList = StringUtils.split(tablePrefix, ",");
            tableName = replaceFirst(tableName, searchList);
        }
        return StringUtils.convertToCamelCase(tableName);
    }

    /**
     * 批量替换前缀
     * 
     * @param replacementm 替换值
     * @param searchList 替换列表
     * @return
     */
    public static String replaceFirst(String replacementm, String[] searchList)
    {
        String text = replacementm;
        for (String searchString : searchList)
        {
            if (replacementm.startsWith(searchString))
            {
                text = replacementm.replaceFirst(searchString, "");
                break;
            }
        }
        return text;
    }

    /**
     * 关键字替换
     * 
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text)
    {
        return RegExUtils.replaceAll(text, "(?:表|极客)", "");
    }

    /**
     * 根据数据库数字类型解析为明确的 Java 类型
     *
     * @param columnType 列类型（如 int(11)、decimal(10,2)、tinyint(1)）
     * @param dataType   已截取的基类型（如 int、decimal、tinyint）
     * @return GenConstants.TYPE_* 常量
     */
    private static String resolveJavaTypeForNumber(String columnType, String dataType) {
        String lower = dataType.toLowerCase();
        String lengthPart = StringUtils.substringBetween(columnType, "(", ")");
        String[] parts = lengthPart != null ? StringUtils.split(lengthPart, ",") : null;

        // 小数类型：decimal/numeric -> BigDecimal
        if ("decimal".equals(lower) || "numeric".equals(lower)) {
            return GenConstants.TYPE_BIGDECIMAL;
        }
        // 浮点：float/double -> Double
        if ("float".equals(lower) || "double".equals(lower)) {
            return GenConstants.TYPE_DOUBLE;
        }
        // 布尔：bit、tinyint(1) -> Boolean
        if ("bit".equals(lower)) {
            return GenConstants.TYPE_BOOLEAN;
        }
        if ("tinyint".equals(lower) && parts != null && parts.length == 1) {
            try {
                if (Integer.parseInt(parts[0].trim()) == 1) {
                    return GenConstants.TYPE_BOOLEAN;
                }
            } catch (NumberFormatException ignored) {
                // fall through to Integer
            }
        }
        // 长整型：bigint -> Long
        if ("bigint".equals(lower)) {
            return GenConstants.TYPE_LONG;
        }
        // 整型：int/integer/smallint/mediumint/tinyint -> Integer
        if ("int".equals(lower) || "integer".equals(lower) || "smallint".equals(lower)
                || "mediumint".equals(lower) || "tinyint".equals(lower)) {
            return GenConstants.TYPE_INTEGER;
        }
        // 默认长整
        return GenConstants.TYPE_LONG;
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType)
    {
        if (StringUtils.indexOf(columnType, "(") > 0)
        {
            return StringUtils.substringBefore(columnType, "(");
        }
        else
        {
            return columnType;
        }
    }

    /**
     * 获取字段长度
     * 
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType)
    {
        if (StringUtils.indexOf(columnType, "(") > 0)
        {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        }
        else
        {
            return 0;
        }
    }
}
