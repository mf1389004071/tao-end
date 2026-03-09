package com.geek.generator.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.velocity.VelocityContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.geek.common.utils.DateUtils;
import com.geek.common.utils.JSON;
import com.geek.common.utils.StringUtils;
import com.geek.generator.constant.GenConstants;
import com.geek.generator.domain.GenColumn;
import com.geek.generator.domain.GenTable;
import com.geek.generator.domain.vo.GenTableVo;

/**
 * 模板处理工具类
 *
 * @author geek
 */
public class VelocityUtils {
    /** 项目空间路径 */
    private static final String PROJECT_PATH = "main/java";

    /** mybatis空间路径 */
    private static final String MYBATIS_PATH = "main/resources/mapper";

    /** 默认上级菜单，系统工具 */
    private static final String DEFAULT_PARENT_MENU_ID = "3";

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(GenTableVo genTableVo) {
        GenTable genTable = genTableVo.getTable();
        String moduleName = genTable.getModuleName();
        String businessName = genTable.getBusinessName();
        String packageName = genTable.getPackageName();
        String tplCategory = genTable.getTplCategory();
        String functionName = genTable.getFunctionName();

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tplCategory", genTable.getTplCategory());
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("functionName", StringUtils.isNotEmpty(functionName) ? functionName : "【请填写功能名称】");
        velocityContext.put("ClassName", genTable.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
        velocityContext.put("businessName", genTable.getBusinessName());
        velocityContext.put("basePackage", getPackagePrefix(packageName));
        velocityContext.put("packageName", packageName);
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", DateUtils.getDate());
        velocityContext.put("pkColumn", genTable.getPkColumn());
        velocityContext.put("importList", getImportList(genTable, genTableVo.getAllGenTableColumns()));
        velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName));
        velocityContext.put("columns", genTable.getColumns());
        velocityContext.put("table", genTable);
        velocityContext.put("tableMap", genTableVo.getTableMap());
        velocityContext.put("tableAliasMap", genTableVo.getTableAliasMap());
        velocityContext.put("columnMap", genTableVo.getColumnMap());
        velocityContext.put("allColumns", genTableVo.getAllGenTableColumns());
        velocityContext.put("joinColunms", genTableVo.getJoinColumns());
        velocityContext.put("joinTablesMate", genTableVo.getJoinTablesMate());
        velocityContext.put("dicts", getDicts(genTable));
        setMenuVelocityContext(velocityContext, genTable);
        if (GenConstants.TPL_TREE.equals(tplCategory)) {
            setTreeVelocityContext(velocityContext, genTable);
        }
        if (GenConstants.TPL_SUB.equals(tplCategory)) {
            setSubVelocityContext(velocityContext, genTable);
        }
        return velocityContext;
    }

    /** 生成菜单使用的 menu_id 起始值（避免与已有菜单冲突，多模块生成时可按需调大） */
    private static final int BASE_MENU_ID = 2000;

    public static void setMenuVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JsonNode paramsObj = JSON.parseObject(options);
        String parentMenuId = getParentMenuId(paramsObj);
        context.put("parentMenuId", parentMenuId);
        context.put("baseMenuId", String.valueOf(BASE_MENU_ID));
        context.put("baseMenuIdButton1", String.valueOf(BASE_MENU_ID + 1));
        context.put("baseMenuIdButton2", String.valueOf(BASE_MENU_ID + 2));
        context.put("baseMenuIdButton3", String.valueOf(BASE_MENU_ID + 3));
        context.put("baseMenuIdButton4", String.valueOf(BASE_MENU_ID + 4));
        context.put("baseMenuIdButton5", String.valueOf(BASE_MENU_ID + 5));
    }

    public static void setTreeVelocityContext(VelocityContext context, GenTable genTable) {
        String options = genTable.getOptions();
        JsonNode paramsObj = JSON.parseObject(options);
        String treeCode = getTreecode(paramsObj);
        String treeParentCode = getTreeParentCode(paramsObj);
        String treeName = getTreeName(paramsObj);

        context.put("treeCode", treeCode);
        context.put("treeParentCode", treeParentCode);
        context.put("treeName", treeName);
        context.put("expandColumn", getExpandColumn(genTable));
        if (paramsObj != null && !paramsObj.isNull()) {
            if (paramsObj.has(GenConstants.TREE_PARENT_CODE)
                    && !paramsObj.get(GenConstants.TREE_PARENT_CODE).isNull()) {
                context.put("tree_parent_code", paramsObj.get(GenConstants.TREE_PARENT_CODE).asText());
            }
            if (paramsObj.has(GenConstants.TREE_NAME)
                    && !paramsObj.get(GenConstants.TREE_NAME).isNull()) {
                context.put("tree_name", paramsObj.get(GenConstants.TREE_NAME).asText());
            }
        }
    }

    public static void setSubVelocityContext(VelocityContext context, GenTable genTable) {
        GenTable subTable = genTable.getSubTable();
        String subTableName = genTable.getSubTableName();
        String subTableFkName = genTable.getSubTableFkName();
        String subClassName = genTable.getSubTable().getClassName();
        String subTableFkClassName = StringUtils.convertToCamelCase(subTableFkName);

        context.put("subTable", subTable);
        context.put("subTableName", subTableName);
        context.put("subTableFkName", subTableFkName);
        context.put("subTableFkClassName", subTableFkClassName);
        context.put("subTableFkclassName", StringUtils.uncapitalize(subTableFkClassName));
        context.put("subClassName", subClassName);
        context.put("subclassName", StringUtils.uncapitalize(subClassName));
        context.put("subImportList", getImportList(genTable.getSubTable()));
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList(String tplCategory, String tplWebType) {
        List<String> templates = new ArrayList<>();
        templates.add("vm/java/domain.java.vm");
        templates.add("vm/java/mapper.java.vm");
        templates.add("vm/java/service.java.vm");
        templates.add("vm/java/serviceImpl.java.vm");
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        templates.add("vm/liquibase/menu-changelog.xml.vm");
        templates.add("vm/liquibase/sys_menu_gen.csv.vm");
        templates.add("vm/liquibase/sys_role_menu_gen.csv.vm");
        templates.add("vm/js/api.js.vm");
        if (GenConstants.TPL_CRUD.equals(tplCategory)) {
            templates.add("vm/vue/index.vue.vm");
        } else if (GenConstants.TPL_TREE.equals(tplCategory)) {
            templates.add("vm/vue/index-tree.vue.vm");
        } else if (GenConstants.TPL_SUB.equals(tplCategory)) {
            templates.add("vm/vue/index.vue.vm");
            templates.add("vm/java/sub-domain.java.vm");
        }
        templates.add("vm/uniapp/edit.vue.vm");
        templates.add("vm/uniapp/list.vue.vm");
        templates.add("vm/uniapp/show.vue.vm");
        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, GenTable genTable) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();

        String javaPath = PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String mybatisPath = MYBATIS_PATH + "/" + moduleName;
        String vuePath = "vue";
        String uniPath = "uniapp";

        if (template.contains("domain.java.vm")) {
            fileName = StringUtils.format("{}/domain/{}.java", javaPath, className);
        } else if (template.contains("sub-domain.java.vm")
                && StringUtils.equals(GenConstants.TPL_SUB, genTable.getTplCategory())) {
            fileName = StringUtils.format("{}/domain/{}.java", javaPath, genTable.getSubTable().getClassName());
        } else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format("{}/mapper/{}Mapper.java", javaPath, className);
        } else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format("{}/service/I{}Service.java", javaPath, className);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = StringUtils.format("{}/service/impl/{}ServiceImpl.java", javaPath, className);
        } else if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format("{}/controller/{}Controller.java", javaPath, className);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = StringUtils.format("{}/{}Mapper.xml", mybatisPath, className);
        } else if (template.contains("menu-changelog.xml.vm")) {
            fileName = StringUtils.format("db/changelog/changelog-2-gen-{}-{}-menu.xml", moduleName, businessName);
        } else if (template.contains("sys_menu_gen.csv.vm")) {
            fileName = StringUtils.format("db/data/sys_menu_gen_{}_{}.csv", moduleName, businessName);
        } else if (template.contains("sys_role_menu_gen.csv.vm")) {
            fileName = StringUtils.format("db/data/sys_role_menu_gen_{}_{}.csv", moduleName, businessName);
        } else if (template.contains("api.js.vm")) {
            fileName = StringUtils.format("{}/api/{}/{}.js", vuePath, moduleName, businessName);
        } else if (template.contains("index.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        } else if (template.contains("entity.js.vm")) {
            fileName = StringUtils.format("{}/entity/{}/{}.js", vuePath, moduleName, businessName);
        } else if (template.contains("edit.vue.vm")) {
            fileName = StringUtils.format("{}/pages/{}/{}/edit.vue", uniPath, moduleName, businessName);
        } else if (template.contains("list.vue.vm")) {
            fileName = StringUtils.format("{}/pages/{}/{}/list.vue", uniPath, moduleName, businessName);
        } else if (template.contains("show.vue.vm")) {
            fileName = StringUtils.format("{}/pages/{}/{}/show.vue", uniPath, moduleName, businessName);
        }
        return fileName;
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    public static String getPackagePrefix(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        return StringUtils.substring(packageName, 0, lastIndex);
    }

    /**
     * 根据列类型获取导入包（含主表+关联表所有列，保证 Instant/BigDecimal 等正确导入）
     *
     * @param genTable  业务表对象
     * @param allColumns 主表与关联表全部列，为 null 时仅用 genTable.getColumns()
     * @return 返回需要导入的包列表
     */
    public static HashSet<String> getImportList(GenTable genTable, List<GenColumn> allColumns) {
        List<GenColumn> columns = (allColumns != null && !allColumns.isEmpty()) ? allColumns : genTable.getColumns();
        GenTable subGenTable = genTable.getSubTable();
        HashSet<String> importList = new HashSet<>();
        if (StringUtils.isNotNull(subGenTable)) {
            importList.add("java.util.List");
        }
        for (GenColumn column : columns) {
            if (column.isSuperColumn()) {
                continue;
            }
            if ("Instant".equals(column.getJavaTypeForField())) {
                importList.add("java.time.Instant");
            } else if (GenConstants.TYPE_BIGDECIMAL.equals(column.getJavaType())
                    || "BigDecimal".equals(column.getJavaTypeForField())) {
                importList.add("java.math.BigDecimal");
            }
        }
        return importList;
    }

    /** @see #getImportList(GenTable, List) */
    public static HashSet<String> getImportList(GenTable genTable) {
        return getImportList(genTable, null);
    }

    /**
     * 根据列类型获取字典组
     *
     * @param genTable 业务表对象
     * @return 返回字典组
     */
    public static String getDicts(GenTable genTable) {
        List<GenColumn> columns = genTable.getColumns();
        Set<String> dicts = new HashSet<String>();
        addDicts(dicts, columns);
        if (StringUtils.isNotNull(genTable.getSubTable())) {
            List<GenColumn> subColumns = genTable.getSubTable().getColumns();
            addDicts(dicts, subColumns);
        }
        return StringUtils.join(dicts, ", ");
    }

    /**
     * 添加字典列表
     *
     * @param dicts   字典列表
     * @param columns 列集合
     */
    public static void addDicts(Set<String> dicts, List<GenColumn> columns) {
        for (GenColumn column : columns) {
            if (!column.isSuperColumn() && StringUtils.isNotEmpty(column.getDictType()) && StringUtils.equalsAny(
                    column.getHtmlType(),
                    new String[] { GenConstants.HTML_SELECT, GenConstants.HTML_RADIO, GenConstants.HTML_CHECKBOX })) {
                dicts.add("'" + column.getDictType() + "'");
            }
        }
    }

    /**
     * 规范化 sys_menu 生成 CSV：确保每行数据列数与表头一致，避免 Liquibase loadData 报错
     * （表头 20 列时，数据行若仅 19 列会报 "Line N has 19 values, Header has 20"）
     *
     * @param csvContent 模板渲染后的 CSV 内容
     * @return 每行列数与表头一致的 CSV 内容
     */
    public static String normalizeSysMenuGenCsv(String csvContent) {
        if (StringUtils.isEmpty(csvContent)) {
            return csvContent;
        }
        String[] lines = csvContent.split("\n", -1);
        if (lines.length < 2) {
            return csvContent;
        }
        int headerCommas = countCommas(lines[0]);
        StringBuilder sb = new StringBuilder(csvContent.length() + 32);
        sb.append(lines[0]);
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.isEmpty()) {
                sb.append('\n');
                continue;
            }
            int lineCommas = countCommas(line);
            if (lineCommas < headerCommas) {
                sb.append('\n').append(line);
                for (int j = lineCommas; j < headerCommas; j++) {
                    sb.append(',');
                }
            } else {
                sb.append('\n').append(line);
            }
        }
        return sb.toString();
    }

    private static int countCommas(CharSequence s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',') {
                n++;
            }
        }
        return n;
    }

    /**
     * 获取权限前缀
     *
     * @param moduleName   模块名称
     * @param businessName 业务名称
     * @return 返回权限前缀
     */
    public static String getPermissionPrefix(String moduleName, String businessName) {
        return StringUtils.format("{}:{}", moduleName, businessName);
    }

    /**
     * 获取上级菜单ID字段
     *
     * @param paramsObj 生成其他选项
     * @return 上级菜单ID字段
     */
    public static String getParentMenuId(JsonNode paramsObj) {
        if (paramsObj != null && !paramsObj.isNull()
                && paramsObj.has(GenConstants.PARENT_MENU_ID)
                && !paramsObj.get(GenConstants.PARENT_MENU_ID).isNull()
                && StringUtils.isNotEmpty(paramsObj.get(GenConstants.PARENT_MENU_ID).asText())) {
            return paramsObj.get(GenConstants.PARENT_MENU_ID).asText();
        }
        return DEFAULT_PARENT_MENU_ID;
    }

    /**
     * 获取树编码
     *
     * @param paramsObj 生成其他选项
     * @return 树编码
     */
    public static String getTreecode(JsonNode paramsObj) {
        if (paramsObj != null && !paramsObj.isNull()
                && paramsObj.has(GenConstants.TREE_CODE)
                && !paramsObj.get(GenConstants.TREE_CODE).isNull()) {
            return StringUtils.toCamelCase(paramsObj.get(GenConstants.TREE_CODE).asText());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取树父编码
     *
     * @param paramsObj 生成其他选项
     * @return 树父编码
     */
    public static String getTreeParentCode(JsonNode paramsObj) {
        if (paramsObj != null && !paramsObj.isNull()
                && paramsObj.has(GenConstants.TREE_PARENT_CODE)
                && !paramsObj.get(GenConstants.TREE_PARENT_CODE).isNull()) {
            return StringUtils.toCamelCase(paramsObj.get(GenConstants.TREE_PARENT_CODE).asText());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取树名称
     *
     * @param paramsObj 生成其他选项
     * @return 树名称
     */
    public static String getTreeName(JsonNode paramsObj) {
        if (paramsObj != null && !paramsObj.isNull()
                && paramsObj.has(GenConstants.TREE_NAME)
                && !paramsObj.get(GenConstants.TREE_NAME).isNull()) {
            return StringUtils.toCamelCase(paramsObj.get(GenConstants.TREE_NAME).asText());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取需要在哪一列上面显示展开按钮
     *
     * @param genTable 业务表对象
     * @return 展开按钮列序号
     */
    public static int getExpandColumn(GenTable genTable) {
        String options = genTable.getOptions();
        JsonNode paramsObj = JSON.parseObject(options);
        String treeName = null;
        if (paramsObj != null && !paramsObj.isNull()
                && paramsObj.has(GenConstants.TREE_NAME)
                && !paramsObj.get(GenConstants.TREE_NAME).isNull()) {
            treeName = paramsObj.get(GenConstants.TREE_NAME).asText();
        }
        if (StringUtils.isEmpty(treeName)) {
            return 0;
        }
        int num = 0;
        for (GenColumn column : genTable.getColumns()) {
            if (column.isList()) {
                num++;
                String columnName = column.getColumnName();
                if (columnName.equals(treeName)) {
                    break;
                }
            }
        }
        return num;
    }
}
