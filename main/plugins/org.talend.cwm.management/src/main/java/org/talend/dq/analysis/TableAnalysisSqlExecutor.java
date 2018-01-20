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
package org.talend.dq.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Display;
import org.talend.core.ITDQRepositoryService;
import org.talend.cwm.exception.AnalysisExecutionException;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dataquality.rules.JoinElement;
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.indicators.IndicatorCommonUtil;
import org.talend.dq.indicators.IndicatorEvaluator;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.RelationalPackage;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TableAnalysisSqlExecutor extends AnalysisExecutor {

    private static Logger log = Logger.getLogger(TableAnalysisSqlExecutor.class);

    private DbmsLanguage dbmsLanguage;

    private String stringDataFilter;

    private Map<ModelElement, Package> schemata = new HashMap<ModelElement, Package>();

    @Override
    protected String createSqlStatement(Analysis analysis) {
        this.cachedAnalysis = analysis;
        AnalysisResult results = analysis.getResults();
        assert results != null;
        try {
            // --- get data filter
            stringDataFilter = ContextHelper.getDataFilterWithoutContext(analysis);
            // --- get all the leaf indicators used for the sql computation
            Collection<Indicator> leafIndicators = IndicatorHelper.getIndicatorLeavesBySingleNode(results);
            // --- create one sql statement for each leaf indicator
            for (Indicator indicator : leafIndicators) {
                if (!createSqlQuery(stringDataFilter, indicator, false)) {
                    log.error(Messages.getString(
                            "ColumnAnalysisSqlExecutor.CREATEQUERYERROR", AnalysisExecutorHelper.getIndicatorName(indicator))); //$NON-NLS-1$
                    return null;
                }
            }
        } catch (AnalysisExecutionException e) {
            log.error(e, e);
            return null;
        }
        return PluginConstant.EMPTY_STRING;
    }

    private boolean isAnalyzedElementValid(Indicator indicator) {
        if (indicator.getAnalyzedElement() == null) {
            traceError(Messages.getString(
                    "ColumnAnalysisSqlExecutor.ANALYSISELEMENTISNULL", AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
            return false;
        }

        // check the AnalyzedElement as set
        NamedColumnSet set = SwitchHelpers.NAMED_COLUMN_SET_SWITCH.doSwitch(indicator.getAnalyzedElement());
        if (set == null) {
            traceError(Messages.getString(
                    "TableAnalysisSqlExecutor.ANALYZEDELEMENTISNOTATABLE", AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
            return false;
        }
        // --- get the schema owner
        if (!belongToSameSchemata(set)) {
            StringBuffer buf = new StringBuffer();
            for (orgomg.cwm.objectmodel.core.Package schema : schemata.values()) {
                buf.append(schema.getName() + " "); //$NON-NLS-1$
            }
            log.error(Messages.getString(
                    "TableAnalysisSqlExecutor.TABLENOTBELONGTOEXISSCHEMA", quote(set.getName()), buf.toString().trim()));//$NON-NLS-1$
            return false;
        }

        return true;
    }

    private boolean isIndicatorDefinitionValid(IndicatorDefinition indicatorDefinition, String indicatorName) {
        if (indicatorDefinition == null) {
            traceError(Messages.getString("ColumnAnalysisSqlExecutor.INTERNALERROR", indicatorName));//$NON-NLS-1$
            return false;
        } else {
            return true;
        }
    }

    private boolean isExpressionValid(Expression sqlGenericExpression, Indicator indicator) {
        if (sqlGenericExpression == null || sqlGenericExpression.getBody() == null) {
            final EClass indicatorEclass = indicator.eClass();
            traceError(Messages
                    .getString(
                            "ColumnAnalysisSqlExecutor.UNSUPPORTEDINDICATOR",//$NON-NLS-1$
                            (indicator.getName() != null ? AnalysisExecutorHelper.getIndicatorName(indicator) : indicatorEclass
                                    .getName())));
            return false;
        } else {
            return true;
        }
    }

    private boolean createSqlQuery(String dataFilterAsString, Indicator indicator, boolean withWhereOfRule)
            throws AnalysisExecutionException {
        if (!isAnalyzedElementValid(indicator)) {
            return Boolean.FALSE;
        }

        IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
        if (!isIndicatorDefinitionValid(indicatorDefinition, AnalysisExecutorHelper.getIndicatorName(indicator))) {
            return Boolean.FALSE;
        }

        Expression sqlGenericExpression = dbms().getSqlExpression(indicatorDefinition);
        if (!isExpressionValid(sqlGenericExpression, indicator)) {
            return Boolean.FALSE;
        }

        // --- get indicator parameters and convert them into sql expression
        List<String> whereExpressionDQRule = new ArrayList<String>();
        final EList<JoinElement> joinConditions = indicator.getJoinConditions();
        if (RulesPackage.eINSTANCE.getWhereRule().equals(indicatorDefinition.eClass())) {
            WhereRule wr = (WhereRule) indicatorDefinition;
            if (withWhereOfRule) {
                whereExpressionDQRule.add(wr.getWhereExpression());
            }
            // MOD scorreia 2009-03-13 copy joins conditions into the indicator
            joinConditions.clear();
            if (!isJoinConditionEmpty(indicator)) {
                for (JoinElement joinelt : wr.getJoins()) {
                    JoinElement joinCopy = EcoreUtil.copy(joinelt);
                    joinConditions.add(joinCopy);
                }
            }
        }

        NamedColumnSet set = SwitchHelpers.NAMED_COLUMN_SET_SWITCH.doSwitch(indicator.getAnalyzedElement());
        String schemaName = getQuotedSchemaName(set);

        // --- normalize table name
        String catalogName = getQuotedCatalogName(set);
        if (catalogName == null && schemaName != null) {
            // try to get catalog above schema
            final Schema parentSchema = SchemaHelper.getParentSchema(set);
            final Catalog parentCatalog = CatalogHelper.getParentCatalog(parentSchema);
            catalogName = parentCatalog != null ? parentCatalog.getName() : null;
        }

        // --- default case
        // allow join
        String joinclause = (!joinConditions.isEmpty()) ? dbms().createJoinConditionAsString(set, joinConditions, catalogName,
                schemaName) : PluginConstant.EMPTY_STRING;

        String setName = dbms().toQualifiedName(catalogName, schemaName, quote(set.getName()));

        String completedSqlString = dbms().fillGenericQueryWithJoin(sqlGenericExpression.getBody(), setName, joinclause);
        // ~
        List<String> whereExpressionAnalysis = new ArrayList<String>();
        if (StringUtils.isNotBlank(dataFilterAsString)) {
            whereExpressionAnalysis.add(dataFilterAsString);
        }
        completedSqlString = addWhereToSqlStringStatement(whereExpressionAnalysis, whereExpressionDQRule, completedSqlString,
                true);

        // completedSqlString is the final query
        String finalQuery = completedSqlString;

        TdExpression instantiateSqlExpression = BooleanExpressionHelper.createTdExpression(dbms().getDbmsName(), finalQuery);
        indicator.setInstantiatedExpression(instantiateSqlExpression);
        return true;
    }

    /**
     * is there are any join confition
     * 
     * @param indicator
     * @return false: when no join conditions set
     */
    private boolean isJoinConditionEmpty(Indicator indicator) {
        return ((WhereRule) indicator.getIndicatorDefinition()).getJoins().isEmpty();
    }

    protected TypedReturnCode<Boolean> belongToSameSchemata(final TdTable tdTable) {
        TypedReturnCode<Boolean> returnCode = new TypedReturnCode<Boolean>(Boolean.TRUE);
        assert tdTable != null;
        if (schemata.get(tdTable) != null) {
            return returnCode;
        }
        // get catalog or schema
        Package schema = ColumnSetHelper.getParentCatalogOrSchema(tdTable);
        if (schema == null) {
            String errorMessage = Messages.getString("TableAnalysisSqlExecutor.NoSchemaOrCatalogFound", tdTable.getName()); //$NON-NLS-1$
            returnCode.setMessage(errorMessage);
            returnCode.setOk(Boolean.FALSE);
            return returnCode;
        }
        schemata.put(tdTable, schema);
        return returnCode;
    }

    @Override
    protected DbmsLanguage dbms() {
        if (this.dbmsLanguage == null) {
            this.dbmsLanguage = createDbmsLanguage();
        }
        return this.dbmsLanguage;
    }

    /**
     * add the where clause to the sql statement.
     * 
     * @param whereExpressionsAnalysis the list of Analysis's where expressions to concatenate (must not be null)
     * @param whereExpressionsDQRule the list of DQRule's where expressions to concatenate (must not be null)
     * @param completedSqlString a generic SQL expression in which the where clause variable will be replaced.
     * @param valid if false add ! before where clause
     * @return the SQL statement with the where clause
     */
    private String addWhereToSqlStringStatement(List<String> whereExpressionsAnalysis, List<String> whereExpressionsDQRule,
            String completedSqlString, boolean valid) {
        String query = completedSqlString;

        // add Analysis's where expression
        StringBuffer buf1 = new StringBuffer();
        for (int i = 0; i < whereExpressionsAnalysis.size(); i++) {
            String exp = whereExpressionsAnalysis.get(i);
            buf1.append(this.surroundWith('(', exp, ')'));
            if (i != whereExpressionsAnalysis.size() - 1 || whereExpressionsDQRule.size() > 0) {
                buf1.append(dbms().and());
            }
        }
        // add DQRule's where expression
        StringBuffer buf2 = new StringBuffer();
        String dqruleWhereClause = buf2.toString();
        for (int i = 0; i < whereExpressionsDQRule.size(); i++) {
            String exp = whereExpressionsDQRule.get(i);
            buf2.append(this.surroundWith('(', exp, ')'));
            if (i != whereExpressionsDQRule.size() - 1) {
                buf2.append(dbms().and());
            }
        }
        // only negate DQRule's where expression
        if (valid) {
            dqruleWhereClause = buf2.toString();
        } else {
            dqruleWhereClause = dbms().not() + "(" + buf2.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        String where = buf1.toString() + dqruleWhereClause;

        if (where != null) {
            query = dbms().addWhereToStatement(query, where);
        }
        return query;
    }

    private String surroundWith(char left, String toSurround, char right) {
        return left + toSurround + right;
    }

    @Override
    protected boolean runAnalysis(Analysis analysis, String sqlStatement) {
        boolean isSuccess = true;

        TypedReturnCode<java.sql.Connection> trc = this.getConnectionBeforeRun(analysis);

        if (!trc.isOk()) {
            log.error(trc.getMessage());
            traceError(Messages.getString(
                    "FunctionalDependencyExecutor.CANNOTEXECUTEANALYSIS", analysis.getName(), trc.getMessage()));//$NON-NLS-1$
            return Boolean.FALSE;
        }

        Connection connection = trc.getObject();
        try {
            Long rowCount = 0L;

            List<Indicator> indicatorList = IndicatorHelper.getIndicatorLeavesBySingleNode(analysis.getResults());

            // execute the row count
            Indicator rowIndicator = indicatorList.get(0);
            isSuccess = executeIndicator(rowIndicator, connection);
            publishDynamicEvent(rowIndicator);
            // remember the row count
            rowCount = rowIndicator.getCount();

            // execute the sql statement for each group of aide and rule
            for (int i = 1; i < indicatorList.size(); i++) {
                final Indicator rule = indicatorList.get(i);

                isSuccess = executeRule((WhereRuleIndicator) rule, connection);
                // if there's no joins, should use the row count as the count.
                if (isJoinConditionEmpty(rule)) {
                    rule.setCount(rowCount);
                }

                publishDynamicEvent(rule);
            }
        } finally {
            ReturnCode rc = closeConnection(analysis, connection);
            if (!rc.isOk()) {
                appendError(rc.getMessage());
                isSuccess = Boolean.FALSE;
            }

        }
        return isSuccess;
    }

    /**
     * DOC yyin Comment method "executeRule".
     * 
     * @param rule
     * @param connection
     * @return
     */
    private boolean executeRule(WhereRuleIndicator rule, Connection connection) {
        // set the connection's catalog
        String catalogName = getCatalogOrSchemaName(rule.getAnalyzedElement());
        if (catalogName != null && needChangeCatalog(connection)) { // check whether null argument can be given
            changeCatalog(catalogName, connection);
        }

        Expression query = dbms().getInstantiatedExpression(rule);
        if (query == null) {
            traceError("Query not executed for indicator: \"" + rule.getName() + "\" "//$NON-NLS-1$//$NON-NLS-2$
                    + "query is null");//$NON-NLS-1$
            return Boolean.FALSE;
        } else {
            try {
                // the current query donot contains where condition, if there're some join condition, execute this query
                // and use this result as the count
                if (!isJoinConditionEmpty(rule)) {
                    List<Object[]> myResultSet = executeQuery(catalogName, connection, query.getBody());
                    rule.setCount(myResultSet);
                }

                // add the where condition to the query, and execute it
                createSqlQuery(stringDataFilter, rule, true);
                query = dbms().getInstantiatedExpression(rule);
                List<Object[]> myResultSet = executeQuery(catalogName, connection, query.getBody());
                // give result to indicator so that it handles the results
                Boolean isExecSuccess = rule.storeSqlResults(myResultSet);
                if (!isExecSuccess) {
                    traceError("Query not executed for indicator: \"" + rule.getName() + "\" "//$NON-NLS-1$//$NON-NLS-2$
                            + "SQL query: " + query.getBody());//$NON-NLS-1$
                    return Boolean.FALSE;
                }
            } catch (Exception e) {
                traceError("Query not executed for indicator: \"" + rule.getName() + "\" "//$NON-NLS-1$//$NON-NLS-2$
                        + "SQL query: " + query.getBody() + ". Exception: " + e.getMessage());//$NON-NLS-1$ //$NON-NLS-2$
                return Boolean.FALSE;
            }
        }
        rule.setComputed(true);
        return Boolean.TRUE;
    }

    /**
     * Added TDQ-8787 publish the related event when one indicator is finished: to refresh the chart with new result of
     * the current indicator
     * 
     * @param rule
     */
    private void publishDynamicEvent(final Indicator rule) {
        final ITDQRepositoryService tdqRepositoryService = AnalysisExecutorHelper.getTDQService();
        if (tdqRepositoryService != null) {
            Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    tdqRepositoryService.publishDynamicEvent(rule, IndicatorCommonUtil.getIndicatorValue(rule));
                }
            });
        }
    }

    private boolean executeIndicator(Indicator indicator, Connection connection) {
        // set the connection's catalog
        String catalogName = getCatalogOrSchemaName(indicator.getAnalyzedElement());
        if (catalogName != null && needChangeCatalog(connection)) { // check whether null argument can be given
            changeCatalog(catalogName, connection);
        }

        Expression query = dbms().getInstantiatedExpression(indicator);
        if (query == null) {
            traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" "//$NON-NLS-1$//$NON-NLS-2$
                    + "query is null");//$NON-NLS-1$
            return Boolean.FALSE;
        } else {
            try {
                // Boolean isExecSuccess = executeQuery(indicator, connection, query.getBody());
                List<Object[]> myResultSet = executeQuery(catalogName, connection, query.getBody());

                // give result to indicator so that it handles the results
                Boolean isExecSuccess = indicator.storeSqlResults(myResultSet);
                if (!isExecSuccess) {
                    traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" "//$NON-NLS-1$//$NON-NLS-2$
                            + "SQL query: " + query.getBody());//$NON-NLS-1$
                    return Boolean.FALSE;
                }
            } catch (Exception e) {
                traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" "//$NON-NLS-1$//$NON-NLS-2$
                        + "SQL query: " + query.getBody() + ". Exception: " + e.getMessage());//$NON-NLS-1$ //$NON-NLS-2$
                return Boolean.FALSE;
            }
        }
        indicator.setComputed(true);
        return Boolean.TRUE;
    }

    private String getCatalogOrSchemaName(ModelElement analyzedElement) {
        Package schema = schemata.get(analyzedElement);
        if (schema == null) {
            log.error(Messages.getString("TableAnalysisSqlExecutor.NOSCHEMAFOUNDFORTABLE", analyzedElement.getName()));//$NON-NLS-1$
            return null;
        }
        // else
        if (RelationalPackage.eINSTANCE.getSchema().equals(schema.eClass())) {
            final Catalog parentCatalog = CatalogHelper.getParentCatalog(schema);
            if (parentCatalog != null) {
                return parentCatalog.getName();
            }
        }
        return schema.getName();
    }

    protected List<Object[]> executeQuery(String catalogName, Connection connection, String queryStmt) throws SQLException {
        if (catalogName != null && needChangeCatalog(connection)) { // check whether null argument can be given
            changeCatalog(catalogName, connection);
        }
        // create query statement
        Statement statement = connection.createStatement();
        // statement.setFetchSize(fetchSize);

        if (continueRun()) {
            statement.execute(queryStmt);
        }

        // get the results
        ResultSet resultSet = statement.getResultSet();
        if (resultSet == null) {
            String mess = Messages.getString("ColumnAnalysisSqlExecutor.NORESULTSETFORTHISSTATEMENT") + queryStmt;//$NON-NLS-1$
            log.warn(mess);
            return null;
        }
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<Object[]> myResultSet = new ArrayList<Object[]>();
        while (resultSet.next()) {
            Object[] result = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                result[i] = resultSet.getObject(i + 1);
            }
            myResultSet.add(result);
        }
        // -- release resources
        resultSet.close();
        statement.close();

        return myResultSet;
    }

    /**
     * DOC xqliu Comment method "getValidStatement". 2009-10-29 bug 9702
     * 
     * @param dataFilterAsString
     * @param indicator
     * @param valid
     * @return
     */
    public String getValidStatement(String dataFilterAsString, Indicator indicator, boolean valid) {
        if (!isAnalyzedElementValid(indicator)) {
            return PluginConstant.EMPTY_STRING;
        }

        IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
        if (!isIndicatorDefinitionValid(indicatorDefinition, AnalysisExecutorHelper.getIndicatorName(indicator))) {
            return PluginConstant.EMPTY_STRING;
        }

        Expression sqlGenericExpression = dbms().getSqlExpression(indicatorDefinition);
        if (!isExpressionValid(sqlGenericExpression, indicator)) {
            return PluginConstant.EMPTY_STRING;
        }

        // --- get indicator parameters and convert them into sql expression
        List<String> whereExpressionAnalysis = new ArrayList<String>();
        if (StringUtils.isNotBlank(dataFilterAsString)) {
            whereExpressionAnalysis.add(dataFilterAsString);
        }
        List<String> whereExpressionDQRule = new ArrayList<String>();
        String setAliasA = PluginConstant.EMPTY_STRING;
        final EList<JoinElement> joinConditions = indicator.getJoinConditions();
        if (RulesPackage.eINSTANCE.getWhereRule().equals(indicatorDefinition.eClass())) {
            WhereRule wr = (WhereRule) indicatorDefinition;
            whereExpressionDQRule.add(wr.getWhereExpression());

            // MOD scorreia 2009-03-13 copy joins conditions into the indicator
            joinConditions.clear();
            if (!isJoinConditionEmpty(indicator)) {
                for (JoinElement joinelt : wr.getJoins()) {
                    JoinElement joinCopy = EcoreUtil.copy(joinelt);
                    joinConditions.add(joinCopy);
                    setAliasA = PluginConstant.EMPTY_STRING.equals(setAliasA) ? joinCopy.getTableAliasA() : setAliasA;
                }
            }
        }

        NamedColumnSet set = SwitchHelpers.NAMED_COLUMN_SET_SWITCH.doSwitch(indicator.getAnalyzedElement());
        String schemaName = getQuotedSchemaName(set);

        // --- normalize table name
        String catalogName = getQuotedCatalogName(set);
        if (catalogName == null && schemaName != null) {
            // try to get catalog above schema
            final Schema parentSchema = SchemaHelper.getParentSchema(set);
            final Catalog parentCatalog = CatalogHelper.getParentCatalog(parentSchema);
            catalogName = parentCatalog != null ? parentCatalog.getName() : null;
        }
        String setName = dbms().toQualifiedName(catalogName, schemaName, quote(set.getName()));

        // ### evaluate SQL Statement depending on indicators ###
        String completedSqlString = null;

        // --- default case
        // allow join
        String joinclause = (!joinConditions.isEmpty()) ? dbms().createJoinConditionAsString(set, joinConditions, catalogName,
                schemaName) : PluginConstant.EMPTY_STRING;

        String genericSql = sqlGenericExpression.getBody();
        setAliasA = PluginConstant.EMPTY_STRING.equals(setAliasA) ? "*" : setAliasA + ".*";//$NON-NLS-1$//$NON-NLS-2$
        genericSql = genericSql.replace("COUNT(*)", setAliasA);//$NON-NLS-1$

        completedSqlString = dbms().fillGenericQueryWithJoin(genericSql, setName, joinclause);
        // ~
        completedSqlString = addWhereToSqlStringStatement(whereExpressionAnalysis, whereExpressionDQRule, completedSqlString,
                valid);

        return completedSqlString;
    }

    /**
     * DOC xqliu Comment method "setCachedAnalysis". 2009-10-29 bug 9702
     * 
     * @param analysis
     */
    public void setCachedAnalysis(Analysis analysis) {
        this.cachedAnalysis = analysis;
    }

    @Override
    protected ReturnCode evaluate(Analysis analysis, java.sql.Connection connection, String sqlStatement) {
        IndicatorEvaluator eval = new IndicatorEvaluator(analysis);
        eval.setMonitor(getMonitor());
        // --- add indicators
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            assert indicator != null;
            NamedColumnSet set = SwitchHelpers.NAMED_COLUMN_SET_SWITCH.doSwitch(indicator.getAnalyzedElement());
            if (set == null) {
                continue;
            }
            // --- get the schema owner
            if (!belongToSameSchemata(set)) {
                setError(Messages.getString("TableAnalysisExecutor.GivenTable", set.getName()));//$NON-NLS-1$
                return new ReturnCode(getErrorMessage(), Boolean.FALSE);
            }
            String setName = dbms().getQueryColumnSetWithPrefix(set);

            eval.storeIndicator(setName, indicator);
        }
        // set it into the evaluator
        eval.setConnection(connection);
        // use pooled connection
        eval.setPooledConnection(POOLED_CONNECTION);

        // when to close connection
        boolean closeAtTheEnd = true;
        Package catalog = schemata.values().iterator().next();
        if (!eval.selectCatalog(catalog.getName())) {
            log.warn("Failed to select catalog " + catalog.getName() + " for connection.");//$NON-NLS-1$//$NON-NLS-2$
        }
        ReturnCode retCode = eval.evaluateIndicators(sqlStatement, closeAtTheEnd);
        if (getMonitor() != null) {
            getMonitor().worked(compIndicatorsWorked);
        }
        return retCode;
    }

    protected boolean belongToSameSchemata(final NamedColumnSet set) {
        assert set != null;
        if (schemata.get(set) != null) {
            return true;
        }
        // get catalog or schema
        Package schema = ColumnSetHelper.getParentCatalogOrSchema(set);
        if (schema == null) {
            setError(Messages.getString("TableAnalysisExecutor.NoSchemaOrCatalogFound", set.getName())); //$NON-NLS-1$
            return false;
        }
        schemata.put(set, schema);
        return true;
    }
}
