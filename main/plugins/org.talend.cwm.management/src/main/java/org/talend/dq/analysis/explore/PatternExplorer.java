// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.analysis.explore;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dq.dbms.GenericSQLHandler;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author scorreia
 * 
 * This class explores the data that matched or did not match a pattern indicator.
 */
public abstract class PatternExplorer extends DataExplorer {

    /**
     * DOC scorreia PatternExplorer constructor comment.
     * 
     */
    public PatternExplorer() {
    }

    @Override
    public Map<String, String> getSubClassQueryMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        // engin on result page
        boolean isSqlEngine = ExecutionLanguage.SQL.equals(analysis.getParameters().getExecutionLanguage());
        initRegularExpressionParameter();

        // MOD zshen 10448 Add menus "view invalid values" and "view valid values" on pattern matching indicator
        map.put(MENU_VIEW_VALID_ROWS, isSqlEngine ? getComment(MENU_VIEW_VALID_ROWS) + getValidRowsStatement() : null);
        map.put(MENU_VIEW_VALID_VALUES, isSqlEngine ? getComment(MENU_VIEW_VALID_VALUES) + getValidValuesStatement() : null);
        map.put(MENU_VIEW_INVALID_ROWS, isSqlEngine ? getComment(MENU_VIEW_INVALID_ROWS) + getInvalidRowsStatement() : null);
        map.put(MENU_VIEW_INVALID_VALUES, isSqlEngine ? getComment(MENU_VIEW_INVALID_VALUES) + getInvalidValuesStatement() : null);
        // ~10448

        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.explore.DataExplorer#NotShowMenu()
     */
    @Override
    protected boolean NotShowMenu() {
        // MOD qiongli 2011-3-30,feature 19192,filter this menu and query for ColumnSet AllMatchIndicator
        return ColumnsetPackage.eINSTANCE.getAllMatchIndicator().isSuperTypeOf(indicator.eClass());
    }

    protected void initRegularExpressionParameter() {
        // The code should be implemented on the sub class
    }

    /**
     * 
     * get the Valid Values Statement.
     * 
     * @return SELECT statement for the invalid Value of select column
     */
    public String getInvalidValuesStatement() {
        // when the indicator is the use define match
        IndicatorDefinition indicatorDefinition = this.indicator.getIndicatorDefinition();
        if (indicatorDefinition instanceof UDIndicatorDefinition) {
            EList<TdExpression> list = ((UDIndicatorDefinition) indicatorDefinition).getViewInvalidValuesExpression();
            return getQueryAfterReplaced(indicatorDefinition, list);
        }

        String regexPatternString = dbmsLanguage.getRegexPatternString(this.indicator);
        String regexCmp = getRegexNotLike(regexPatternString);
        // add null as invalid rows
        String nullClause = dbmsLanguage.or() + columnName + dbmsLanguage.isNull();
        // mzhao TDQ-4967 add "(" and ")" for regex and null clause.
        String pattCondStr = "(" + regexCmp + nullClause + ")";//$NON-NLS-1$//$NON-NLS-2$
        return getValuesStatement(columnName, pattCondStr);
    }

    abstract protected String getRegexNotLike(String regexPatternString);

    /**
     * 
     * get the Valid Values Statement.
     * 
     * @return SELECT statement for the valid Value of select column
     */
    public String getValidValuesStatement() {
        // when the indicator is the use define match
        IndicatorDefinition indicatorDefinition = this.indicator.getIndicatorDefinition();
        if (indicatorDefinition instanceof UDIndicatorDefinition) {
            EList<TdExpression> list = ((UDIndicatorDefinition) indicatorDefinition).getViewValidValuesExpression();
            return getQueryAfterReplaced(indicatorDefinition, list);
        }

        String regexPatternString = dbmsLanguage.getRegexPatternString(this.indicator);
        String regexCmp = getRegexLike(regexPatternString);
        return getValuesStatement(columnName, regexCmp);
    }

    abstract protected String getRegexLike(String regexPatternString);

    /**
     * get the Invalid Rows Statement.
     * 
     * @return
     */
    public String getInvalidRowsStatement() {
        // when the indicator is the use define match
        IndicatorDefinition indicatorDefinition = this.indicator.getIndicatorDefinition();
        if (indicatorDefinition instanceof UDIndicatorDefinition) {
            EList<TdExpression> list = ((UDIndicatorDefinition) indicatorDefinition).getViewInvalidRowsExpression();
            return getQueryAfterReplaced(indicatorDefinition, list);
        }

        String regexPatternString = dbmsLanguage.getRegexPatternString(this.indicator);
        String regexCmp = getRegexNotLike(regexPatternString);
        // add null as invalid rows
        String nullClause = dbmsLanguage.or() + columnName + dbmsLanguage.isNull();
        // mzhao TDQ-4967 add "(" and ")" for regex and null clause.
        String pattCondStr = "(" + regexCmp + nullClause + ")";//$NON-NLS-1$//$NON-NLS-2$
        return getRowsStatement(pattCondStr);
    }

    /**
     * get the Valid Rows Statement.
     * 
     * @return
     */
    public String getValidRowsStatement() {
        // when the indicator is the use define match
        IndicatorDefinition indicatorDefinition = this.indicator.getIndicatorDefinition();
        if (indicatorDefinition instanceof UDIndicatorDefinition) {
            EList<TdExpression> list = ((UDIndicatorDefinition) indicatorDefinition).getViewValidRowsExpression();
            return getQueryAfterReplaced(indicatorDefinition, list);
        }

        String regexPatternString = dbmsLanguage.getRegexPatternString(this.indicator);
        String regexCmp = getRegexLike(regexPatternString);
        return getRowsStatement(regexCmp);
    }

    /**
     * get the Query After Replaced.
     * 
     * @param indicatorDefinition
     * @param list
     * @return String
     */
    public String getQueryAfterReplaced(IndicatorDefinition indicatorDefinition, EList<TdExpression> list) {
        String sql = PluginConstant.EMPTY_STRING;
        TdExpression tdExp =
                dbmsLanguage.getSqlExpression(indicatorDefinition, dbmsLanguage.getDbmsName(), list,
                dbmsLanguage.getDbVersion());
        sql = tdExp.getBody();
        String dataFilterClause = getDataFilterClause();
        if (!dataFilterClause.equals(PluginConstant.EMPTY_STRING)) {
            sql = sql.replace(GenericSQLHandler.WHERE_CLAUSE, dbmsLanguage.where() + "(" + dataFilterClause + ")"); //$NON-NLS-1$ //$NON-NLS-2$
            sql = sql.replace(GenericSQLHandler.AND_WHERE_CLAUSE, dbmsLanguage.and() + "(" + dataFilterClause + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            sql = sql.replace(GenericSQLHandler.WHERE_CLAUSE, PluginConstant.EMPTY_STRING);
            sql = sql.replace(GenericSQLHandler.AND_WHERE_CLAUSE, PluginConstant.EMPTY_STRING);
        }
        ModelElement analyzedElement = indicator.getAnalyzedElement();
        String tableName = getFullyQualifiedTableName(analyzedElement);
        sql = dbmsLanguage.fillGenericQueryWithColumnsAndTable(sql, dbmsLanguage.quote(analyzedElement.getName()), tableName);
        if (sql.indexOf(GenericSQLHandler.GROUP_BY_ALIAS) != -1) {
            sql = sql.replace(GenericSQLHandler.GROUP_BY_ALIAS, dbmsLanguage.quote(analyzedElement.getName()));
        }
        return sql;
    }

    public boolean isImplementRegexFunction(String menuLabel) {
        if (menuLabel == null) {
            return false;
        }
        String regexPatternString = dbmsLanguage.getRegexPatternString(this.indicator);
        if (menuLabel.equals(MENU_VIEW_VALID_ROWS) || menuLabel.equals(MENU_VIEW_VALID_VALUES)) {
            if (getRegexLike(regexPatternString) != null) {
                return true;
            }
        } else if (menuLabel.equals(DataExplorer.MENU_VIEW_INVALID_ROWS) || menuLabel.equals(MENU_VIEW_INVALID_VALUES)) {
            if (getRegexNotLike(regexPatternString) != null) {
                return true;
            }
        }
        return false;

    }

}
