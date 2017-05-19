// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.analysis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.cwm.exception.AnalysisExecutionException;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.RowMatchingIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * 
 * executes multicolumn comparison.
 */
public class RowMatchingAnalysisExecutor extends ColumnAnalysisSqlExecutor {

    private static Logger log = Logger.getLogger(RowMatchingAnalysisExecutor.class);

    private String catalogOrSchema = null;

    private boolean reversion = false;

    private HashMap<Indicator, Boolean> indiReversionMap;

    private void reset() {
        catalogOrSchema = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    public String createSqlStatement(Analysis analysis) {
        this.cachedAnalysis = analysis;
        this.reset();

        EList<Indicator> indicators = analysis.getResults().getIndicators();
        if (indiReversionMap == null) {
            indiReversionMap = new HashMap<Indicator, Boolean>();
        }
        for (int i = 0; i < indicators.size(); i++) {
            Indicator indicator = indicators.get(i);
            if (i == 1) {
                indiReversionMap.put(indicator, Boolean.TRUE);
            } else {
                indiReversionMap.put(indicator, Boolean.FALSE);
            }
            instantiateQuery(indicator);
        }

        // no query to return, here we only instantiate several SQL queries
        return PluginConstant.EMPTY_STRING;
    }

    /**
     * DOC scorreia Comment method "instantiateQuery".
     * 
     * @param indicator
     */
    private boolean instantiateQuery(Indicator indicator) {
        // indicator.reset(); // rli reset will clear columnSetA and columnSetB, we will lost our analysedElement
        // information. So comment it. // scorreia -> changed the implementation of reset() so that it can now be called
        // (but is not need, hence we keep it commented)
        if (ColumnsetPackage.eINSTANCE.getRowMatchingIndicator().equals(indicator.eClass())) {
            RowMatchingIndicator rowMatchingIndicator = (RowMatchingIndicator) indicator;
            EList<TdColumn> columnSetA = rowMatchingIndicator.getColumnSetA();
            EList<TdColumn> columnSetB = rowMatchingIndicator.getColumnSetB();
            if (columnSetA.size() != columnSetB.size()) {
                traceError("Cannot compare two column sets with different size");//$NON-NLS-1$ 
                return Boolean.FALSE;
                // break;
            }

            IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
            Expression sqlGenericExpression = dbms().getSqlExpression(indicatorDefinition);

            boolean useNulls = false; // TODO scorreia create an indicator for each option
            Expression instantiatedSqlExpression = createInstantiatedSqlExpression(sqlGenericExpression, columnSetA, columnSetB,
                    useNulls, indicator);
            indicator.setInstantiatedExpression(instantiatedSqlExpression);
            return true;
        }
        traceError("Unhandled given indicator: " + AnalysisExecutorHelper.getIndicatorName(indicator));//$NON-NLS-1$
        return Boolean.FALSE;
    }

    /**
     * DOC scorreia Comment method "createInstantiatedSqlExpression".
     * 
     * @param sqlGenericExpression
     * @param columnSetA
     * @param columnSetB
     * @param useNulls
     * @return
     */
    private Expression createInstantiatedSqlExpression(Expression sqlGenericExpression, EList<TdColumn> columnSetA,
            EList<TdColumn> columnSetB, boolean useNulls, Indicator indicator) {
        // MOD scorreia 2009-05-25 allow to compare elements from the same table
        // aliases of tables
        String aliasA = "A"; //$NON-NLS-1$
        String aliasB = "B"; //$NON-NLS-1$

        // MOD xqliu 2009-06-16 bug 7334
        String dataFilterA = ContextHelper.getDataFilterWithoutContext(this.cachedAnalysis, AnalysisHelper.DATA_FILTER_A);
        String dataFilterB = ContextHelper.getDataFilterWithoutContext(this.cachedAnalysis, AnalysisHelper.DATA_FILTER_B);

        // MOD qiongli 2011-12-28 TDQ-4240.get the reversion value from a Map before generating sqlExpression.
        reversion = indiReversionMap != null && indiReversionMap.get(indicator) != null ? indiReversionMap.get(indicator)
                .booleanValue() : false;
        if (reversion) {
            dataFilterA = ContextHelper.getDataFilterWithoutContext(this.cachedAnalysis, AnalysisHelper.DATA_FILTER_B);
            dataFilterB = ContextHelper.getDataFilterWithoutContext(this.cachedAnalysis, AnalysisHelper.DATA_FILTER_A);
        }
        String tableNameA = addDataFilterWithTableName(getTableName(columnSetA), dataFilterA) + " " + aliasA; //$NON-NLS-1$
        String tableNameB = addDataFilterWithTableName(getTableName(columnSetB), dataFilterB) + " " + aliasB; //$NON-NLS-1$
        // ~

        // Generic SQL expression is something like:
        // SELECT COUNT(*) FROM <%=__TABLE_NAME__%> LEFT JOIN <%=__TABLE_NAME_2__%> ON (<%=__JOIN_CLAUSE__%>) WHERE
        // (<%=__WHERE_CLAUSE__%>)
        String genericSQL = sqlGenericExpression.getBody();
        String joinClause = createJoinClause(aliasA, columnSetA, aliasB, columnSetB, useNulls);
        String whereClause = createWhereClause(aliasB, columnSetB);
        if (useNulls) {
            // add a where clause to avoid the equality of rows fully null (i.e. rows like "null,null,null"
            whereClause += dbms().and() + '(' + createNotNullCondition(aliasA, columnSetA) + ')';
        }

        String instantiatedSQL = dbms().fillGenericQueryWithJoin(genericSQL, tableNameA, tableNameB, joinClause, whereClause);
        Expression instantiatedExpression = CoreFactory.eINSTANCE.createExpression();
        instantiatedExpression.setLanguage(sqlGenericExpression.getLanguage());
        instantiatedExpression.setBody(instantiatedSQL);

        return instantiatedExpression;
    }

    private String addDataFilterWithTableName(String tableName, String dataFilter) {
        if (dataFilter == null || dataFilter.trim().equals(PluginConstant.EMPTY_STRING)) {
            return tableName;
        }
        return "(SELECT * FROM " + tableName + " WHERE ( " + dataFilter + " ))"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * DOC scorreia Comment method "createWhereClause".
     * 
     * @param tableNameB
     * @param columnSetB
     * @return
     */
    private String createWhereClause(String tableNameB, EList<TdColumn> columnSetB) {
        final String isNull = dbms().isNull();
        final String and = dbms().and();
        return conditionOnAllColumns(tableNameB, columnSetB, isNull, and);
    }

    private String createNotNullCondition(String tableNameB, EList<TdColumn> columnSetB) {
        final String isNotNull = dbms().isNotNull();
        final String or = dbms().or();
        return conditionOnAllColumns(tableNameB, columnSetB, isNotNull, or);
    }

    /**
     * DOC scorreia Comment method "conditionOnAllColumns".
     * 
     * @param tableName
     * @param columnSet
     * @param isNull
     * @param and
     * @return
     */
    private String conditionOnAllColumns(String tableName, EList<TdColumn> columnSet, final String isNull, final String and) {
        int size = columnSet.size();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(tableName).append(PluginConstant.DOT_STRING).append(getQuotedColumnName(columnSet.get(i)))
                    .append(isNull);
            if (i != size - 1) {
                builder.append(and);
            }
        }
        return builder.toString();
    }

    /**
     * Method "createJoinClause".
     * 
     * @param tableNameA already quoted table name
     * @param columnSetA
     * @param tableNameB already quoted table name
     * @param columnSetB
     * @return
     */
    private String createJoinClause(String tableNameA, EList<TdColumn> columnSetA, String tableNameB, EList<TdColumn> columnSetB,
            final boolean useNulls) {
        StringBuilder builder = new StringBuilder();
        int size = columnSetA.size();
        for (int i = 0; i < size; i++) {
            String colA = tableNameA + PluginConstant.DOT_STRING + getQuotedColumnName(columnSetA.get(i));
            String colB = tableNameB + PluginConstant.DOT_STRING + getQuotedColumnName(columnSetB.get(i));
            builder.append(" (").append(colA).append(dbms().equal()).append(colB); //$NON-NLS-1$
            if (useNulls) { // allow to identify rows like ('a', null) = ('a', null)
                builder.append(dbms().or()).append(bothNull(colA, colB));
            }
            builder.append(") "); //$NON-NLS-1$
            if (i != size - 1) {
                builder.append(dbms().and());
            }
        }

        return builder.toString();
    }

    /**
     * Method "bothNull".
     * 
     * @param colA a column name from table A
     * @param colB a column name from table B
     * @return colA IS NULL AND colB IS NULL
     */
    private String bothNull(String colA, String colB) {
        StringBuilder builder = new StringBuilder();
        builder.append(" (").append(colA).append(dbms().isNull()).append(dbms().and()).append(colB).append(dbms().isNull()) //$NON-NLS-1$
                .append(") "); //$NON-NLS-1$
        return builder.toString();
    }

    /**
     * DOC scorreia Comment method "getTableName".
     * 
     * @param columnSetA
     * @return
     */
    private String getTableName(EList<TdColumn> columnSetA) {
        String tableName = null;
        for (TdColumn column : columnSetA) {
            if (column == null) {
                continue;
            }
            if (column.eIsProxy()) {
                column = (TdColumn) EObjectHelper.resolveObject(column);
            }
            if (belongToSameSchemata(column)) {
                ColumnSet columnSetOwner = ColumnHelper.getColumnOwnerAsColumnSet(column);

                if (columnSetOwner == null) {
                    log.error(Messages.getString("FunctionalDependencyExecutor.COLUMNSETOWNERISNULL", column.getName()));//$NON-NLS-1$
                    continue;
                } else {
                    tableName = dbms().getQueryColumnSetWithPrefix(column);

                    // ~11005
                    this.catalogOrSchema = getCatalogOrSchemaName(column);
                    break; // all columns should belong to the same table
                }
            } else {
                log.error(getErrorMessage());
            }
        }
        return quote(tableName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#runAnalysis(org.talend.dataquality.analysis.Analysis,
     * java.lang.String)
     */
    @Override
    protected boolean runAnalysis(Analysis analysis, String sqlStatement) {
        boolean isSuccess = Boolean.TRUE;

        TypedReturnCode<java.sql.Connection> trc = this.getConnectionBeforeRun(analysis);

        if (!trc.isOk()) {
            log.error(trc.getMessage());
            setError(trc.getMessage());
            traceError(Messages.getString(
                    "FunctionalDependencyExecutor.CANNOTEXECUTEANALYSIS", analysis.getName(), trc.getMessage()));//$NON-NLS-1$
            return Boolean.FALSE;
        }

        Connection connection = trc.getObject();
        try {

            // execute the sql statement for each indicator
            EList<Indicator> indicators = analysis.getResults().getIndicators();
            EList<Indicator> deactivatedIndicators = analysis.getParameters().getDeactivatedIndicators();
            for (Indicator indicator : indicators) {
                if (deactivatedIndicators.contains(indicator)) {
                    continue; // do not evaluate this indicator
                }
                // set the connection's catalog
                if (this.catalogOrSchema != null && needChangeCatalog(connection)) { // check whether null argument can be given
                    changeCatalog(this.catalogOrSchema, connection);
                }
                Expression query = dbms().getInstantiatedExpression(indicator);

                if (query == null) {
                    traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" "//$NON-NLS-1$//$NON-NLS-2$
                            + "query is null");//$NON-NLS-1$
                    isSuccess = Boolean.FALSE;
                    continue;
                }

                try {
                    Boolean isExecSuccess = executeQuery(indicator, connection, query);
                    indicator.setComputed(true);
                    if (!isExecSuccess) {
                        traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" "//$NON-NLS-1$//$NON-NLS-2$
                                + "SQL query: " + query.getBody());//$NON-NLS-1$
                        isSuccess = Boolean.FALSE;
                        continue;
                    }
                } catch (AnalysisExecutionException e) {
                    traceError(e.getMessage());
                    isSuccess = Boolean.FALSE;
                    continue;
                }

            }

        } finally {
            ReturnCode rc = closeConnection(analysis, connection);
            if (!rc.isOk()) {
                isSuccess = Boolean.FALSE;
            }
        }
        return isSuccess;
    }

    /**
     * DOC scorreia Comment method "executeQuery".
     * 
     * @param indicator
     * @param connection
     * @param query
     * @return
     * @throws AnalysisExecutionException
     */
    private boolean executeQuery(Indicator indicator, Connection connection, Expression query) throws AnalysisExecutionException {
        try {
            List<Object[]> myResultSet = executeQuery(catalogOrSchema, connection, query.getBody());
            String tableName = getAnalyzedTable(indicator);

            // MOD xqliu 2009-06-16 bug 7334
            // set data filter here
            reversion = indiReversionMap != null && indiReversionMap.get(indicator) != null ? indiReversionMap.get(indicator)
                    .booleanValue() : false;
            String stringDataFilter = reversion ? ContextHelper.getDataFilterWithoutContext(this.cachedAnalysis,
                    AnalysisHelper.DATA_FILTER_B) : ContextHelper.getDataFilterWithoutContext(this.cachedAnalysis,
                    AnalysisHelper.DATA_FILTER_A);
            List<String> whereClauses = new ArrayList<String>();
            if (stringDataFilter != null && !stringDataFilter.trim().equals(PluginConstant.EMPTY_STRING)) {
                whereClauses.add(stringDataFilter);
            }
            // ~
            // give result to indicator so that it handles the results
            boolean ok = indicator.storeSqlResults(myResultSet);
            // get row count and store it in indicator
            Long count = getCount(cachedAnalysis, "*", tableName, catalogOrSchema, whereClauses); //$NON-NLS-1$
            ok = ok && count != null;
            indicator.setCount(count);

            // compute matching count
            if (ColumnsetPackage.eINSTANCE.getRowMatchingIndicator().equals(indicator.eClass())) {
                RowMatchingIndicator rowMatchingIndicator = (RowMatchingIndicator) indicator;
                Long notMatchingValueCount = rowMatchingIndicator.getNotMatchingValueCount();
                ok = ok && notMatchingValueCount != null;
                if (ok) {
                    rowMatchingIndicator.setMatchingValueCount(count - notMatchingValueCount);
                }
            }

            return ok;
        } catch (SQLException e) {
            log.error(e, e);
            // MOD TDQ-8388 return the exact error message
            throw new AnalysisExecutionException(e.getMessage());
        }

    }

    /**
     * Method "getAnalyzedTable".
     * 
     * @param indicator
     * @return the table name (within quotes)
     */
    private String getAnalyzedTable(Indicator indicator) {
        // MOD mzhao bug 11481. get table name with catalog or schema prefix.
        String analyzedTableName = null;
        ColumnSet columnSetOwner = (ColumnSet) indicator.getAnalyzedElement();
        if (columnSetOwner == null) {
            log.error(Messages.getString(
                    "RowMatchingAnalysisExecutor.COLUMNSETOWNERISNULL", AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
        } else {
            if (columnSetOwner.eIsProxy()) {
                columnSetOwner = (ColumnSet) EObjectHelper.resolveObject(columnSetOwner);
            }
            String schemaName = getQuotedSchemaName(columnSetOwner);
            String table = quote(columnSetOwner.getName());
            String catalogName = getQuotedCatalogName(columnSetOwner);
            if (catalogName == null && schemaName != null) {
                // try to get catalog above schema
                final Schema parentSchema = SchemaHelper.getParentSchema(columnSetOwner);
                final Catalog parentCatalog = CatalogHelper.getParentCatalog(parentSchema);
                catalogName = parentCatalog != null ? parentCatalog.getName() : null;
            }
            analyzedTableName = dbms().toQualifiedName(catalogName, schemaName, table);
        }

        return analyzedTableName;
    }

    @Override
    protected boolean checkAnalyzedElements(final Analysis analysis, AnalysisContext context) {
        AnalysisHandler analysisHandler = new AnalysisHandler();
        analysisHandler.setAnalysis(analysis);

        // TODO How to handle the context.getAnalysedElements()???
        // for (ModelElement node : context.getAnalysedElements()) {
        // TdColumn column = SwitchHelpers.COLUMN_SWITCH.doSwitch(node);
        //
        // // --- Check that each analyzed element has at least one indicator
        // if (analysisHandler.getIndicators(column).size() == 0) {
        // this.errorMessage = "Each column must have at least one indicator, "
        // + "please select some indicator(s) to compute on each column!";
        // return false;
        // }
        //
        // // --- get the data provider
        // TdDataProvider dp = DataProviderHelper.getTdDataProvider(column);
        // if (!isAccessWith(dp)) {
        // this.errorMessage = "All columns must belong to the same connection! Remove column " + column.getName()
        // + " from this analysis! It does not belong to \"" + dataprovider.getName() + "\"";
        // return false;
        // }
        // }
        return true;
    }

}
