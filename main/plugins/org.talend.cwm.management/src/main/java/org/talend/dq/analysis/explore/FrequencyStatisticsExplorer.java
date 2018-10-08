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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.RangeRestriction;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dq.dbms.DB2DbmsLanguage;
import org.talend.dq.dbms.GenericSQLHandler;
import org.talend.dq.dbms.HiveDbmsLanguage;
import org.talend.dq.dbms.SybaseASEDbmsLanguage;
import org.talend.utils.sql.Java2SqlType;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class FrequencyStatisticsExplorer extends DataExplorer {

    protected String getFreqRowsStatement() {

        IndicatorDefinition indicatorDefinition = this.indicator.getIndicatorDefinition();
        if (indicatorDefinition instanceof UDIndicatorDefinition) {
            return getQueryForUDIndicator(indicatorDefinition);
        }

        String clause = PluginConstant.EMPTY_STRING;

        TdColumn column = (TdColumn) indicator.getAnalyzedElement();
        int javaType = column.getSqlDataType().getJavaDataType();

        if (Java2SqlType.isTextInSQL(javaType)) {
            clause = getInstantiatedClause();
        } else if (Java2SqlType.isDateInSQL(javaType)) {
            // MOD scorreia 2009-09-22 first check whether the value is null
            if (entity.isLabelNull()) {
                clause = getInstantiatedClause();
            } else {
                IndicatorParameters parameters = indicator.getParameters();
                if (parameters != null) {
                    DateParameters dateParameters = parameters.getDateParameters();
                    if (dateParameters != null) {
                        DateGrain dateGrain = dateParameters.getDateAggregationType();
                        clause = entity.isLabelNull() ? getInstantiatedClause() : getClauseWithDate(dateGrain, clause);
                    } else {
                        clause = getInstantiatedClause();
                    }
                } else {
                    clause = getInstantiatedClause();
                }
            }

        } else if (Java2SqlType.isNumbericInSQL(javaType)) {
            IndicatorParameters parameters = indicator.getParameters();
            if (parameters != null) {
                // handle bins
                Domain bins = parameters.getBins();
                if (bins != null) {
                    // rangeStrings = getBinsAsGenericString(bins.getRanges());
                    final EList<RangeRestriction> ranges = bins.getRanges();
                    for (RangeRestriction rangeRestriction : ranges) {
                        // find the rangeLabel
                        if (entity.getLabel() != null && entity.getLabel().equals(rangeRestriction.getName())) {
                            clause = createWhereClause(rangeRestriction);
                            break;
                        }
                    }
                } else {// MOD hcheng 2009-05-18.Bug 7377,Frequency indicator,when bins is null,handle as textual data
                    clause = getInstantiatedClause();
                }
            } else { // MOD scorreia 2009-05-13. Bug 7235
                // no parameter set: handle as textual data
                clause = getInstantiatedClause();
            }
        } else {
            clause = getDefaultQuotedStatement(PluginConstant.EMPTY_STRING); // no quote here
        }

        return "SELECT * FROM " + getFullyQualifiedTableName(column) + dbmsLanguage.where() + inBrackets(clause) //$NON-NLS-1$
                + andDataFilterClause();
    }

    /**
     * get Query For User define indicator.
     * 
     * @param indicatorDefinition
     * @return
     */
    private String getQueryForUDIndicator(IndicatorDefinition indicatorDefinition) {
        String sql = PluginConstant.EMPTY_STRING;
        EList<TdExpression> list = ((UDIndicatorDefinition) indicatorDefinition).getViewRowsExpression();
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
        ModelElement analyzedElement = this.indicator.getAnalyzedElement();
        String tableName = getFullyQualifiedTableName(analyzedElement);
        sql = sql.replace(GenericSQLHandler.TABLE_NAME, tableName);
        sql = sql.replace(GenericSQLHandler.COLUMN_NAMES, dbmsLanguage.quote(analyzedElement.getName()));
        if (sql.indexOf(GenericSQLHandler.UDI_INDICATOR_VALUE) != -1) {
            Object key = entity.getKey();
            if (key == null) {
                sql = sql.replace("= " + GenericSQLHandler.UDI_INDICATOR_VALUE, "IS NULL"); //$NON-NLS-1$ //$NON-NLS-2$                
            } else {
                sql = sql.replace(GenericSQLHandler.UDI_INDICATOR_VALUE, "'" + key + "'"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        if (sql.indexOf(GenericSQLHandler.GROUP_BY_ALIAS) != -1) {
            sql = sql.replace(GenericSQLHandler.GROUP_BY_ALIAS, dbmsLanguage.quote(analyzedElement.getName()));
        }
        return sql;
    }

    /**
     * get Claus With Date.
     * 
     * @param dateGrain
     * @param clause
     * @return
     */
    @SuppressWarnings("fallthrough")
    private String getClauseWithDate(DateGrain dateGrain, String clause) {
        switch (dateGrain) {
        case DAY:
            clause = dbmsLanguage.extractDay(this.columnName) + dbmsLanguage.equal() + getDayCharacters(entity.getLabel());
            // no break
        case WEEK:
            if (clause.length() == 0) { // needs week to identify the row
                clause = concatWhereClause(clause, dbmsLanguage.extractWeek(this.columnName) + dbmsLanguage.equal()
                        + getWeekCharacters(entity.getLabel()));
            }
            // no break
        case MONTH:
            clause = concatWhereClause(clause, dbmsLanguage.extractMonth(this.columnName) + dbmsLanguage.equal()
                    + getMonthCharacters(dateGrain, entity.getLabel()));
            // no break
        case QUARTER:
            if (clause.length() == 0) { // need quarter to identify the row
                clause = concatWhereClause(clause, dbmsLanguage.extractQuarter(this.columnName) + dbmsLanguage.equal()
                        + getQuarterCharacters(entity.getLabel()));
            }
            // no break
        case YEAR:
            clause = concatWhereClause(clause, dbmsLanguage.extractYear(this.columnName) + dbmsLanguage.equal()
                    + getYearCharacters(entity.getLabel()));
            break;
        case NONE:
        default:
            clause = getDefaultQuotedStatement("'"); //$NON-NLS-1$
            break;
        }
        return clause;
    }

    /**
     * DOC scorreia Comment method "createWhereClause".
     * 
     * @param rangeRestriction
     * @return
     */
    private String createWhereClause(RangeRestriction rangeRestriction) {
        double max = Double.valueOf(DomainHelper.getMaxValue(rangeRestriction));
        double min = Double.valueOf(DomainHelper.getMinValue(rangeRestriction));
        String whereClause = columnName + dbmsLanguage.greaterOrEqual() + min + dbmsLanguage.and() + columnName
                + dbmsLanguage.less() + max;
        return whereClause;
    }

    private String getDefaultQuotedStatement(String quote) {
        return entity.isLabelNull() ? dbmsLanguage.quote(this.columnName) + dbmsLanguage.isNull() : dbmsLanguage
                .quote(this.columnName) + dbmsLanguage.equal() + quote + entity.getLabel() + quote;
    }

    /**
     * DOC scorreia Comment method "getQuarterCharacters".
     * 
     * @param label
     * @return
     */
    private String getQuarterCharacters(String label) {
        return label.substring(label.length() - 1);
    }

    /**
     * DOC scorreia Comment method "getYearCharacters".
     * 
     * @param label
     * @return
     */
    private String getYearCharacters(String label) {
        if (label != null && label.length() >= 4) {
            return label.substring(0, 4);
        }

        return null;
    }

    /**
     * DOC scorreia Comment method "getMonthCharacters".
     * 
     * @param dateGrain
     * 
     * @param label
     * @return
     */
    private String getMonthCharacters(DateGrain dateGrain, String label) {
        switch (dateGrain) {
        case DAY:
        case WEEK:
            // week and day are the two last digits
            return label.substring(label.length() - 4, label.length() - 2);
        case MONTH:
            return label.substring(label.length() - 2);
        default:
            break;
        }
        return null;
    }

    /**
     * DOC scorreia Comment method "getWeekCharacters".
     * 
     * @param label
     * @return
     */
    private String getWeekCharacters(String label) {
        return label.substring(label.length() - 2);
    }

    /**
     * DOC scorreia Comment method "getDayCharacters".
     * 
     * @param label
     * @return
     */
    private String getDayCharacters(String label) {
        return label.substring(label.length() - 2);
    }

    /**
     * DOC scorreia Comment method "concatWhereClause".
     * 
     * @param clause
     * @return
     */
    private String concatWhereClause(String clause, String whereclause) {
        String and = (clause.length() == 0) ? PluginConstant.EMPTY_STRING : dbmsLanguage.and();
        clause = clause + and + whereclause;
        return clause;
    }

    @Override
    public Map<String, String> getSubClassQueryMap() {
        Map<String, String> map = new HashMap<String, String>();
        // MOD zshen feature 12919 adapt to pop-menu for Jave engin on result page
        boolean isJavaEngine = AnalysisHelper.isJavaExecutionEngine(this.analysis);
        map.put(MENU_VIEW_ROWS, !isJavaEngine ? getComment(MENU_VIEW_ROWS) + getFreqRowsStatement() : null);

        return map;
    }

    /**
     * Method "getInstantiatedClause".
     * 
     * @return the where clause from the instantiated query
     */
    protected String getInstantiatedClause() {
        // get function which convert data into a pattern
        TdColumn column = (TdColumn) indicator.getAnalyzedElement();
        int javaType = column.getSqlDataType().getJavaDataType();
        // MOD mzhao bug 9681 2009-11-09

        Object value = null;
        if (Java2SqlType.isNumbericInSQL(javaType) && dbmsLanguage instanceof DB2DbmsLanguage) {
            value = entity.getKey();
        } else if (Java2SqlType.isNumbericInSQL(javaType) && dbmsLanguage instanceof SybaseASEDbmsLanguage) {
            value = entity.getKey();
        } else if (Java2SqlType.isNumbericInSQL(javaType) && dbmsLanguage instanceof HiveDbmsLanguage) {
            value = entity.getKey();
        } else {
            value = "'" + entity.getKey() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        String clause = entity.isLabelNull() ? columnName + dbmsLanguage.isNull() : columnName + dbmsLanguage.equal() + value;
        return clause;
    }

}
