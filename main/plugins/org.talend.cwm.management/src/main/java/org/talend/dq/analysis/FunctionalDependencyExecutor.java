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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.cwm.exception.AnalysisExecutionException;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnDependencyIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC jet class global comment. Detailled comment
 */
public class FunctionalDependencyExecutor extends ColumnAnalysisSqlExecutor {

    private static Logger log = Logger.getLogger(FunctionalDependencyExecutor.class);

    private String catalogName = null;

    private String schemaName = null;

    @Override
    protected boolean check(Analysis analysis) {
        return true;

    }

    @Override
    protected boolean runAnalysis(Analysis analysis, String sqlStatement) {
        Boolean runStatus = Boolean.TRUE;
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

                Expression query = dbms().getInstantiatedExpression(indicator);
                if (query == null) {
                    // TODO internationalize the string.
                    traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" "//$NON-NLS-1$//$NON-NLS-2$
                            + "query is null");//$NON-NLS-1$
                    runStatus = Boolean.FALSE;
                    continue;
                }

                try {
                    boolean exeStatus = executeQuery(indicator, connection, query);
                    if (!exeStatus) {
                        // TODO internationalize the string.
                        traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" "//$NON-NLS-1$//$NON-NLS-2$
                                + "SQL query: " + query.getBody());//$NON-NLS-1$
                        runStatus = Boolean.FALSE;
                        continue;
                    }
                } catch (AnalysisExecutionException e) {
                    traceError(e.getMessage());
                    runStatus = Boolean.FALSE;
                    continue;
                }
                indicator.setComputed(true);

            }
        } finally {
            ReturnCode rc = closeConnection(analysis, connection);
            if (!rc.isOk()) {
                runStatus = Boolean.FALSE;
            }
        }
        return runStatus;
    }

    private boolean executeQuery(Indicator indicator, Connection connection, Expression query) throws AnalysisExecutionException {
        try {

            List<Object[]> myResultSet = executeQuery(catalogName, connection, query.getBody());
            indicator.storeSqlResults(myResultSet);

        } catch (SQLException e) {
            log.error(e, e);
            // MOD TDQ-8388 return the exact error message
            throw new AnalysisExecutionException(e.getMessage());
        }

        return true;
    }

    @Override
    public String createSqlStatement(Analysis analysis) {
        this.cachedAnalysis = analysis;

        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(analysis);
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            instantiateQuery(indicator, dbmsLanguage);
        }

        // no query to return, here we only instantiate several SQL queries
        return PluginConstant.EMPTY_STRING;
    }

    private boolean instantiateQuery(Indicator indicator, DbmsLanguage dbmsLanguage) {
        // (but is not need, hence we keep it commented)

        if (ColumnsetPackage.eINSTANCE.getColumnDependencyIndicator().equals(indicator.eClass())) {
            ColumnDependencyIndicator rowMatchingIndicator = (ColumnDependencyIndicator) indicator;
            TdColumn columnA = rowMatchingIndicator.getColumnA();
            TdColumn columnB = rowMatchingIndicator.getColumnB();

            IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
            // MOD qiongli 2012-3-14 TDQ-4433 in this case(import by "button" from low veresion and no import
            // SystemIndicator),the IndicatorDefinition maybe a proxy,should reset it.
            if (indicatorDefinition == null || indicatorDefinition.eIsProxy()) {
                indicatorDefinition = DefinitionHandler.getInstance().getIndicatorDefinition("Functional Dependency"); //$NON-NLS-1$
                indicator.setIndicatorDefinition(indicatorDefinition);
            }
            Expression sqlGenericExpression = dbms().getSqlExpression(indicatorDefinition);

            Expression instantiatedSqlExpression = createInstantiatedSqlExpression(sqlGenericExpression, columnA, columnB,
                    dbmsLanguage);
            indicator.setInstantiatedExpression(instantiatedSqlExpression);
            return true;
        }
        traceError(Messages.getString(
                "FunctionalDependencyExecutor.UNHANDLEDGIVENINDICATOR", AnalysisExecutorHelper.getIndicatorName(indicator)));//$NON-NLS-1$
        return Boolean.FALSE;
    }

    /**
     * DOC jet Comment method "createInstantiatedSqlExpression".
     * 
     * @param sqlGenericExpression
     * @param columnA
     * @param columnB
     * @param dbmsLanguage
     * @param useNulls
     * @return
     */
    private Expression createInstantiatedSqlExpression(Expression sqlGenericExpression, TdColumn columnA, TdColumn columnB,
            DbmsLanguage dbmsLanguage) {
        assert columnA != null;
        assert columnB != null;

        String genericSQL = sqlGenericExpression.getBody();

        // MOD zshen 11005: SQL syntax error for all analysis on Informix databases in Talend Open Profiler

        String table = getTableNameFromColumn(columnA);
        String instantiatedSQL = dbms().fillGenericQueryWithColumnsABAndTable(genericSQL, dbmsLanguage.quote(columnA.getName()),
                dbmsLanguage.quote(columnB.getName()), table);

        // ~11005
        List<String> whereClauses = new ArrayList<String>();

        String dataFilter = ContextHelper.getDataFilterWithoutContext(cachedAnalysis);
        if (!StringUtils.isEmpty(dataFilter)) {
            whereClauses.add(dataFilter);
        }

        instantiatedSQL = dbms().addWhereToSqlStringStatement(instantiatedSQL, whereClauses);

        Expression instantiatedExpression = CoreFactory.eINSTANCE.createExpression();
        instantiatedExpression.setLanguage(sqlGenericExpression.getLanguage());
        instantiatedExpression.setBody(instantiatedSQL);
        return instantiatedExpression;
    }

    private String getTableNameFromColumn(TdColumn column) {

        ColumnSet columnSetOwner = ColumnHelper.getColumnOwnerAsColumnSet(column);
        if (columnSetOwner == null) {
            log.error(Messages.getString("FunctionalDependencyExecutor.COLUMNSETOWNERISNULL", column.getName()));//$NON-NLS-1$
        } else {
            // this is so bad code
            Package pack = ColumnSetHelper.getParentCatalogOrSchema(columnSetOwner);
            if (SwitchHelpers.SCHEMA_SWITCH.doSwitch(pack) != null) {
                schemaName = pack.getName();
                Package catalog = ColumnSetHelper.getParentCatalogOrSchema(pack);
                // MOD mzhao 2010-02-10 Fix a NEP.
                if (catalog != null && SwitchHelpers.CATALOG_SWITCH.doSwitch(catalog) != null) {
                    catalogName = catalog.getName();
                }
            }
            if (SwitchHelpers.CATALOG_SWITCH.doSwitch(pack) != null) {
                catalogName = pack.getName();
            }
            return dbms().toQualifiedName(catalogName, schemaName, columnSetOwner.getName());

        }
        return null;
    }

}
