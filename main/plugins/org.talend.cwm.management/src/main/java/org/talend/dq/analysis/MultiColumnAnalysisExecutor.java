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

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.dq.helper.ContextHelper;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class MultiColumnAnalysisExecutor extends ColumnAnalysisSqlExecutor {

    private static Logger log = Logger.getLogger(MultiColumnAnalysisExecutor.class);

    private String catalogOrSchema = null;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dq.analysis.ColumnAnalysisSqlExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    public String createSqlStatement(Analysis analysis) {
        this.cachedAnalysis = analysis;
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
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
    private void instantiateQuery(Indicator indicator) {
        if (ColumnsetPackage.eINSTANCE.getColumnSetMultiValueIndicator().isSuperTypeOf(indicator.eClass())) {
            ColumnSetMultiValueIndicator colSetMultValIndicator = (ColumnSetMultiValueIndicator) indicator;
            final EList<ModelElement> analyzedColumns = colSetMultValIndicator.getAnalyzedColumns();
            final EList<String> numericFunctions = initializeNumericFunctions(colSetMultValIndicator);
            final EList<String> dateFunctions = initializeDateFunctions(colSetMultValIndicator);

            // ADD msjian 2011-5-30 17479: Excel Odbc connection can not run well on the correlation analysis
            // note: this feature is not supported now, if support, delete this
            final String caseStr = "SUM(CASE WHEN {0} IS NULL THEN 1 ELSE 0 END)";//$NON-NLS-1$
            if ("EXCEL".equals(dbms().getDbmsName()) //$NON-NLS-1$
                    && (dateFunctions.contains(caseStr) || numericFunctions.contains(caseStr))) {
                setError(Messages.getString("MultiColumnAnalysisExecutor.errMessage")); //$NON-NLS-1$
                Display.getDefault().syncExec(new Runnable() {

                    public void run() {
                        MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                Messages.getString("MultiColumnAnalysisExecutor.errTitle"), getErrorMessage()); //$NON-NLS-1$
                        return;
                    }
                });
            }
            // ~

            // get indicator definition
            final Expression sqlGenericExpression = dbms().getSqlExpression(indicator.getIndicatorDefinition());

            // separate nominal from numeric columns
            List<String> nominalColumns = new ArrayList<String>();
            for (ModelElement column : colSetMultValIndicator.getNominalColumns()) {
                nominalColumns.add(getQuotedColumnName(column));
            }
            List<String> computedColumns = new ArrayList<String>();
            for (ModelElement column : colSetMultValIndicator.getNumericColumns()) {
                // call functions for each column
                for (String f : numericFunctions) {
                    computedColumns.add(replaceVariablesLow(f, getQuotedColumnName(column)));
                }
            }
            for (ModelElement column : colSetMultValIndicator.getDateColumns()) {
                // call functions for each column
                for (String f : dateFunctions) {
                    computedColumns.add(replaceVariablesLow(f, getQuotedColumnName(column)));
                }
            }
            // add count(*)
            computedColumns.add(colSetMultValIndicator.getCountAll());

            // MOD msjian TDQ-7254: fix the columnset analysis run get error. the columnset analysis don't need to
            // consider the datamining type.
            List<String> columns = new ArrayList<String>();

            // if the analysis type is columnset, use analyzed columns
            if (AnalysisType.COLUMN_SET == cachedAnalysis.getParameters().getAnalysisType()) {
                for (ModelElement column : analyzedColumns) {
                    columns.add(getQuotedColumnName(column));
                }
            } else {
                columns = nominalColumns;
            }
            // TDQ-7254~

            String selectItems = createSelect(columns, computedColumns);

            String grpByClause = createGroupBy(columns);

            // all columns must belong to the same table
            TdColumn firstColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(analyzedColumns.get(0));
            String tableName = dbms().getQueryColumnSetWithPrefix(firstColumn);
            this.catalogOrSchema = dbms().getCatalogOrSchemaName(firstColumn);
            // definition is SELECT &lt;%=__COLUMN_NAMES__%> FROM &lt;%=__TABLE_NAME__%> GROUP BY
            // &lt;%=__GROUP_BY_ALIAS__%>
            String sqlExpr = dbms().fillGenericQueryWithColumnTableAndAlias(sqlGenericExpression.getBody(), selectItems,
                    tableName, grpByClause);

            // handle data filter
            String stringDataFilter = ContextHelper.getDataFilterWithoutContext(cachedAnalysis);
            if (stringDataFilter == null) {
                stringDataFilter = PluginConstant.EMPTY_STRING;
            }
            sqlExpr = dbms().addWhereToStatement(sqlExpr, stringDataFilter);

            indicator.setInstantiatedExpression(BooleanExpressionHelper.createTdExpression(sqlGenericExpression.getLanguage(),
                    sqlExpr));
            // MOD qiongli 2011-3-30 feature 19192.allow drill down for sql engine.
            if (ColumnsetPackage.eINSTANCE.getSimpleStatIndicator().isSuperTypeOf(indicator.eClass())) {
                SimpleStatIndicator simpleIndicator = (SimpleStatIndicator) indicator;
                // MOD TDQ-7287 lost some columns(type!=norminal) when view values in column set ana. yyin 20130514
                String columnsName = createSelect(columns, new ArrayList<String>());
                for (Indicator leafIndicator : simpleIndicator.getLeafIndicators()) {
                    final Expression leafSqlGenericExpression = dbms().getSqlExpression(leafIndicator.getIndicatorDefinition());
                    String leafSqlExpr = dbms().fillGenericQueryWithColumnTableAndAlias(leafSqlGenericExpression.getBody(),
                            columnsName, tableName, grpByClause);
                    leafSqlExpr = dbms().addWhereToStatement(leafSqlExpr, stringDataFilter);
                    leafIndicator.setInstantiatedExpression(BooleanExpressionHelper.createTdExpression(
                            leafSqlGenericExpression.getLanguage(), leafSqlExpr));

                }
            }
        }
    }

    /**
     * DOC scorreia Comment method "initializeNumericFunctions".
     * 
     * @param indicator
     * @return
     */
    private EList<String> initializeNumericFunctions(ColumnSetMultiValueIndicator indicator) {
        final EList<String> numericFunctions = indicator.getNumericFunctions();
        if (!numericFunctions.isEmpty()) { // could be already set
            return numericFunctions;
        }
        final IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
        final List<String> aggregate1argFunctions = dbms().getAggregate1argFunctions(indicatorDefinition);
        numericFunctions.addAll(aggregate1argFunctions);
        return numericFunctions;
    }

    /**
     * DOC scorreia Comment method "initializeNumericFunctions".
     * 
     * @param indicator
     * @return
     */
    private EList<String> initializeDateFunctions(ColumnSetMultiValueIndicator indicator) {
        final EList<String> dateFunctions = indicator.getDateFunctions();
        if (!dateFunctions.isEmpty()) { // could be already set
            return dateFunctions;
        }
        final IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
        final List<String> date1argFunctions = dbms().getDate1argFunctions(indicatorDefinition);
        dateFunctions.addAll(date1argFunctions);
        return dateFunctions;
    }

    /**
     * DOC scorreia Comment method "getTableName".
     * 
     * @param analyzedColumns
     * @return the quoted table name
     */

    /*
     * tableName = columnSetOwner.getName(); Package pack = PackageHelper.getCatalogOrSchema(columnSetOwner); if (pack
     * == null) { log.error("No Catalog or Schema found for column set owner: " + tableName); continue; // do not break
     * until we find the owner } this.catalogOrSchema = pack.getName(); break; // all columns should belong to the same
     * table
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#runAnalysis(org.talend.dataquality.analysis.Analysis,
     * java.lang.String)
     */
    @Override
    protected boolean runAnalysis(Analysis analysis, String sqlStatement) {
        boolean isSuccess = true;

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
            for (Indicator indicator : indicators) {
                indicator.prepare();
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
                    Boolean isExeSuccess = executeQuery(indicator, connection, query);
                    if (!isExeSuccess) {
                        traceError("Query not executed for indicator: \"" + AnalysisExecutorHelper.getIndicatorName(indicator) + "\" "//$NON-NLS-1$//$NON-NLS-2$
                                + ((query == null) ? "query is null" : "SQL query: " + query.getBody()));//$NON-NLS-1$//$NON-NLS-2$
                        isSuccess = Boolean.FALSE;
                        continue;
                    }
                } catch (Exception e) {
                    traceError(e.getMessage());
                    isSuccess = Boolean.FALSE;
                    continue;
                }

                indicator.setComputed(true);
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
     * DOC scorreia Comment method "createGroupBy".
     * 
     * @param nominalColumns
     * @return
     */
    private String createGroupBy(List<String> nominalColumns) {
        StringBuilder builder = new StringBuilder();
        final int nbNomColumns = nominalColumns.size();
        for (int i = 0; i < nbNomColumns; i++) {
            builder.append(nominalColumns.get(i));
            if (i != nbNomColumns - 1) {
                builder.append(COMMA);
            }
        }
        return builder.toString();
    }

    private static final String COMMA = ","; //$NON-NLS-1$

    /**
     * DOC scorreia Comment method "createSelect".
     * 
     * @param nominalColumns
     * @param computedColumns
     * @return
     */
    private String createSelect(List<String> nominalColumns, List<String> computedColumns) {
        StringBuilder builder = new StringBuilder();
        final int nbNomColumns = nominalColumns.size();
        final int nbComColumns = computedColumns.size();
        for (int i = 0; i < nbNomColumns; i++) {
            builder.append(nominalColumns.get(i));
            if (i != nbNomColumns - 1 || nbComColumns != 0) {
                builder.append(COMMA);
            }
        }
        for (int i = 0; i < nbComColumns; i++) {
            builder.append(computedColumns.get(i));
            if (i != nbComColumns - 1) {
                builder.append(COMMA);
            }
        }
        return builder.toString();
    }

    /**
     * DOC scorreia Comment method "executeQuery".
     * 
     * @param indicator
     * @param connection
     * @param query
     * @return
     */
    private boolean executeQuery(Indicator indicator, Connection connection, Expression query) {
        try {
            List<Object[]> myResultSet = executeQuery(catalogOrSchema, connection, query.getBody());

            // give result to indicator so that it handles the results
            return indicator.storeSqlResults(myResultSet);
        } catch (SQLException e) {
            log.error(e, e);
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dq.analysis.ColumnAnalysisExecutor#checkAnalyzedElements(org.talend.dataquality.analysis.Analysis,
     * org.talend.dataquality.analysis.AnalysisContext)
     */
    @Override
    protected boolean checkAnalyzedElements(Analysis analysis, AnalysisContext context) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.ColumnAnalysisExecutor#check(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    protected boolean check(Analysis analysis) {

        boolean check = super.check(analysis);
        if (!check) {
            return false;
        } else {
            EList<Indicator> indicators = analysis.getResults().getIndicators();
            for (Indicator indicator : indicators) {
                if (indicator instanceof AllMatchIndicator) {
                    // MOD qiongli 2011-6-16 bug 21768,column set dosen't support pattern in sql engine.
                    setError(Messages.getString("MultiColumnAnalysisExecutor.noSupportSqlEngine")); //$NON-NLS-1$ 
                    return false;
                }
            }
        }
        return true;
    }
}
