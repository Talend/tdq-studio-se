// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.dbms;

import org.talend.dataquality.PluginConstant;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class GenericSQLHandler {

    public static final String UDI_ORDER_BY = "<ORDER_BY_TEXT_FIELD>"; //$NON-NLS-1$

    public static final String UDI_GROUP_BY = "<GROUP_BY_TEXT_FIELD>"; //$NON-NLS-1$

    public static final String UDI_SECOND_COLUMN = "<SECOND_COLUMN_EXPRESSION_TEXT_FIELD>"; //$NON-NLS-1$

    public static final String UDI_FIRST_COLUMN = "<FIRST_COLUMN_EXPRESSION_TEXT_FIELD>"; //$NON-NLS-1$

    public static final String UDI_COLUMN = "<COLUMN_EXPRESSION_TEXT_FIELD>"; //$NON-NLS-1$

    public static final String UDI_WHERE = "<WHERE_TEXT_FIELD>"; //$NON-NLS-1$

    public static final String UDI_MATCHING = "<MATCHING_EXPRESSION_TEXT_FIELD>"; //$NON-NLS-1$

    public static final String UDI_INDICATOR_VALUE = "<%=__INDICATOR_VALUE__%>"; //$NON-NLS-1$

    public static final String AND_WHERE_CLAUSE = "<%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    public static final String WHERE_CLAUSE = "<%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    public static final String TABLE_NAME = "<%=__TABLE_NAME__%>"; //$NON-NLS-1$

    public static final String TABLE_NAME2 = "<%=__TABLE_NAME_2__%>"; //$NON-NLS-1$

    public static final String COLUMN_NAMES = "<%=__COLUMN_NAMES__%>"; //$NON-NLS-1$

    public static final String GROUP_BY_ALIAS = "<%=__GROUP_BY_ALIAS__%>"; //$NON-NLS-1$

    private static final String LIMIT_ROW = "<%=__LIMIT_ROW__%>"; //$NON-NLS-1$

    public static final String PATTERN_EXPRESSION = "<%=__PATTERN_EXPR__%>"; //$NON-NLS-1$

    public static final String JOIN_CLAUSE = "<%=__JOIN_CLAUSE__%>"; //$NON-NLS-1$

    private static final String LIMIT_OFFSET = "<%=__LIMIT_OFFSET__%>"; //$NON-NLS-1$

    private static final String LIMIT_ROW_PLUS_OFFSET = "<%=__LIMIT_ROW_PLUS_OFFSET__%>"; //$NON-NLS-1$

    public static final String COLUMN_NAMES_A = "<%=__COLUMN_NAME_A__%>"; //$NON-NLS-1$

    public static final String COLUMN_NAMES_B = "<%=__COLUMN_NAME_B__%>"; //$NON-NLS-1$

    private final String originalSQL;

    private String sqlString;

    /**
     * GenericSQLHandler constructor comment.
     * 
     * @param genericString a generic string
     */
    public GenericSQLHandler(final String genericString) {
        assert genericString != null;
        this.sqlString = new String(genericString);
        this.originalSQL = genericString;
    }

    /**
     * Getter for originalSQL.
     * 
     * @return the originalSQL
     */
    public String getOriginalSQL() {
        return this.originalSQL;
    }

    public GenericSQLHandler replaceTable(String table) {
        sqlString = sqlString.replace(TABLE_NAME, table);
        return this;
    }

    public GenericSQLHandler replaceColumn(String columns) {
        sqlString = sqlString.replace(COLUMN_NAMES, columns);
        return this;
    }

    public GenericSQLHandler replaceGroupByAlias(String groupByAliases) {
        sqlString = sqlString.replace(GROUP_BY_ALIAS, groupByAliases);
        return this;
    }

    public GenericSQLHandler replaceColumnTableAlias(String columns, String table, String groupByAliases) {
        return this.replaceColumnTable(columns, table).replaceGroupByAlias(groupByAliases);
    }

    public GenericSQLHandler replaceColumnTable(String columns, String table) {
        return this.replaceColumn(columns).replaceTable(table);
    }

    public boolean containsAndClause() {
        return sqlString.contains(AND_WHERE_CLAUSE);
    }

    public boolean containsWhereClause() {
        return sqlString.contains(WHERE_CLAUSE);
    }

    public GenericSQLHandler replaceWhere(String whereClause) {
        sqlString = sqlString.replace(WHERE_CLAUSE, whereClause);
        return this;
    }

    public GenericSQLHandler replaceAndClause(String whereClause) {
        sqlString = sqlString.replace(AND_WHERE_CLAUSE, whereClause);
        return this;
    }

    public GenericSQLHandler replaceWithJoin(String tableNameA, String tableNameB, String joinClause, String whereClause) {
        sqlString = sqlString.replace(TABLE_NAME, tableNameA).replace(TABLE_NAME2, tableNameB).replace(JOIN_CLAUSE, joinClause)
                .replace(WHERE_CLAUSE, whereClause);
        return this;
    }

    public GenericSQLHandler replaceJoinClause(String joinclause) {
        sqlString = sqlString.replace(JOIN_CLAUSE, joinclause);
        return this;
    }

    public GenericSQLHandler replaceLimitOffset(String colName, String table, String limitRow, String offset,
            String limitRowPlusOffset) {
        this.replaceColumnTable(colName, table);
        sqlString = this.sqlString.replace(LIMIT_ROW, limitRow).replace(LIMIT_OFFSET, offset)
                .replace(LIMIT_ROW_PLUS_OFFSET, limitRowPlusOffset);
        return this;
    }

    public GenericSQLHandler replacePattern(String regexp) {
        sqlString = sqlString.replace(PATTERN_EXPRESSION, regexp);
        return this;
    }

    public GenericSQLHandler replaceColumnA(String regexp) {
        sqlString = sqlString.replace(COLUMN_NAMES_A, regexp);
        return this;
    }

    public GenericSQLHandler replaceColumnB(String regexp) {
        sqlString = sqlString.replace(COLUMN_NAMES_B, regexp);
        return this;
    }

    public GenericSQLHandler replaceColumnTablePattern(String columns, String table, String regexp) {
        return this.replaceColumnTable(columns, table).replacePattern(regexp);
    }

    /**
     * Method "getSqlString".
     * 
     * @return the modified sql string
     */
    public String getSqlString() {
        return this.sqlString;
    }

    /**
     * Method "createGenericSqlWithRegexFunction".
     * 
     * @param function a two arguments function.
     * @return the full generic statement
     */
    public String createGenericSqlWithRegexFunction(String function) {
        return "SELECT COUNT(CASE WHEN " + function + "(" + COLUMN_NAMES + "," + PATTERN_EXPRESSION //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ") THEN 1 END), COUNT(*) FROM " + TABLE_NAME + " " + WHERE_CLAUSE; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public boolean containsUDIOrderBy() {
        return sqlString.contains(UDI_ORDER_BY);
    }

    public GenericSQLHandler replaceUDIOrderBy(String UDIOrderBy) {
        sqlString = sqlString.replace(UDI_ORDER_BY, UDIOrderBy);
        return this;
    }

    public boolean containsUDIGroupBy() {
        return sqlString.contains(UDI_GROUP_BY);
    }

    public GenericSQLHandler replaceUDIGroupBy(String UDIGroupBy) {
        sqlString = sqlString.replace(UDI_GROUP_BY, UDIGroupBy);
        return this;
    }

    public boolean containsUDISecondColumn() {
        return sqlString.contains(UDI_SECOND_COLUMN);
    }

    public GenericSQLHandler replaceUDISecondColumn(String UDISecondColumn) {
        sqlString = sqlString.replace(UDI_SECOND_COLUMN, UDISecondColumn);
        return this;
    }

    public boolean containsUDIFirstColumn() {
        return sqlString.contains(UDI_FIRST_COLUMN);
    }

    public GenericSQLHandler replaceUDIFirstColumn(String UDIFirstColumn) {
        sqlString = sqlString.replace(UDI_FIRST_COLUMN, UDIFirstColumn);
        return this;
    }

    public boolean containsUDIColumn() {
        return sqlString.contains(UDI_COLUMN);
    }

    public GenericSQLHandler replaceUDIColumn(String UDIColumn) {
        sqlString = sqlString.replace(UDI_COLUMN, UDIColumn);
        return this;
    }

    public boolean containsUDIWhere() {
        return sqlString.contains(UDI_WHERE);
    }

    public GenericSQLHandler replaceUDIWhere(String UDIWhere) {
        sqlString = sqlString.replace(UDI_WHERE, "(" + UDIWhere + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        return this;
    }

    public boolean containsUDIMatching() {
        return sqlString.contains(UDI_MATCHING);
    }

    public GenericSQLHandler replaceUDIMatching(String UDIMatching) {
        sqlString = sqlString.replace(UDI_MATCHING, UDIMatching);
        return this;
    }

    public GenericSQLHandler replaceUDIQueryToMatch() {
        sqlString = sqlString.replace("WHERE " + UDI_WHERE + PluginConstant.SPACE_STRING //$NON-NLS-1$ 
                + AND_WHERE_CLAUSE, WHERE_CLAUSE);
        sqlString = sqlString.replace("AND " + UDI_WHERE + PluginConstant.SPACE_STRING //$NON-NLS-1$ 
                + AND_WHERE_CLAUSE, AND_WHERE_CLAUSE);
        sqlString = sqlString.replace("ORDER BY " + UDI_ORDER_BY, PluginConstant.SPACE_STRING); //$NON-NLS-1$ 
        sqlString = sqlString.replace("GROUP BY " + UDI_GROUP_BY, PluginConstant.SPACE_STRING); //$NON-NLS-1$ 
        sqlString = sqlString.replace("WHERE  AND", "WHERE "); //$NON-NLS-1$ //$NON-NLS-2$
        sqlString = sqlString.replace("AND  " + AND_WHERE_CLAUSE, AND_WHERE_CLAUSE); //$NON-NLS-1$ 
        sqlString = sqlString.replace("AND ()", " "); //$NON-NLS-1$ //$NON-NLS-2$ 
        sqlString = sqlString.replace("( AND", "("); //$NON-NLS-1$ //$NON-NLS-2$
        sqlString = sqlString.replace("AND )", ")"); //$NON-NLS-1$ //$NON-NLS-2$
        sqlString = sqlString.replace("( AND )", " "); //$NON-NLS-1$ //$NON-NLS-2$
        sqlString = sqlString.replace("NOT   " + AND_WHERE_CLAUSE, "NOT " + WHERE_CLAUSE); //$NON-NLS-1$ //$NON-NLS-2$
        sqlString = sqlString.replace("NOT ( ) " + AND_WHERE_CLAUSE, "NOT " + WHERE_CLAUSE); //$NON-NLS-1$ //$NON-NLS-2$
        sqlString = sqlString.replace("WHERE   " + AND_WHERE_CLAUSE, WHERE_CLAUSE); //$NON-NLS-1$ 
        sqlString = sqlString.replace("WHERE  " + AND_WHERE_CLAUSE, WHERE_CLAUSE); //$NON-NLS-1$ 
        sqlString = sqlString.replace("WHERE () " + AND_WHERE_CLAUSE, WHERE_CLAUSE); //$NON-NLS-1$ 
        sqlString = sqlString.replace("WHERE " + WHERE_CLAUSE, WHERE_CLAUSE); //$NON-NLS-1$ 
        sqlString = sqlString.replace("WHERE  = <%=__INDICATOR_VALUE__%> <%=__AND_WHERE_CLAUSE__%>", WHERE_CLAUSE); //$NON-NLS-1$
        sqlString = sqlString.replace("GROUP BY   ORDER BY", " ORDER BY"); //$NON-NLS-1$ //$NON-NLS-2$
        sqlString = sqlString.trim();
        if (sqlString.endsWith("ORDER BY")) { //$NON-NLS-1$
            sqlString = sqlString.replace("ORDER BY", PluginConstant.EMPTY_STRING); //$NON-NLS-1$
        }
        sqlString = sqlString.trim();
        if (sqlString.endsWith("GROUP BY")) { //$NON-NLS-1$
            sqlString = sqlString.replace("GROUP BY", PluginConstant.EMPTY_STRING); //$NON-NLS-1$
        }
        sqlString = sqlString.trim();
        return this;
    }
}
