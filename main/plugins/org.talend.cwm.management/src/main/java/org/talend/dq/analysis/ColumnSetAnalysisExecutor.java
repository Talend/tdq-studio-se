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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dq.dbms.GenericSQLHandler;
import org.talend.dq.indicators.ColumnSetIndicatorEvaluator;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Classifier;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class ColumnSetAnalysisExecutor extends AnalysisExecutor {

    private static Logger log = Logger.getLogger(ColumnSetAnalysisExecutor.class);

    protected boolean isDelimitedFile = false;

    protected boolean isMdm = false;

    /**
     * DOC yyi 2011-02-24 17871:delimitefile.
     */
    public ColumnSetAnalysisExecutor(boolean isDelimitedFile, boolean isMdm) {
        super();
        this.isDelimitedFile = isDelimitedFile;
        this.isMdm = isMdm;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#runAnalysis(org.talend.dataquality.analysis.Analysis,
     * java.lang.String)
     */
    @Override
    protected boolean runAnalysis(Analysis analysis, String sqlStatement) {
        ColumnSetIndicatorEvaluator eval = createIndicatorEvaluator(analysis);
        eval.setMonitor(getMonitor());
        // --- add indicators
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            if (ColumnsetPackage.eINSTANCE.getColumnSetMultiValueIndicator().isSuperTypeOf(indicator.eClass())) {
                ColumnSetMultiValueIndicator colSetMultValIndicator = (ColumnSetMultiValueIndicator) indicator;
                colSetMultValIndicator.prepare();
                eval.storeIndicator(indicator.getName(), colSetMultValIndicator);
            }
        }

        TypedReturnCode<java.sql.Connection> connection = null;
        // MOD yyi 2011-02-22 17871:delimitefile
        if (!isDelimitedFile) {
            connection = initConnection(analysis, eval);
            if (!connection.isOk()) {
                return false;
            }
        }

        // when to close connection
        boolean closeAtTheEnd = true;
        ReturnCode rc = eval.evaluateIndicators(sqlStatement, closeAtTheEnd);

        // MOD gdbu 2011-8-12 : file delimited connection is null
        // close connection
        if (connection != null) {
            if (POOLED_CONNECTION) {
                // release the pooled connection
                resetConnectionPool(analysis);
            } else {
                ConnectionUtils.closeConnection(connection.getObject());
            }
        }
        if (!rc.isOk()) {
            traceError(rc.getMessage());
        }
        if (getMonitor() != null) {
            getMonitor().worked(compIndicatorsWorked);
        }
        return rc.isOk();
    }

    /**
     * DOC zshen Comment method "initConnection".
     * 
     * @param analysis
     * @param eval
     * @return
     */
    protected TypedReturnCode<java.sql.Connection> initConnection(Analysis analysis, ColumnSetIndicatorEvaluator eval) {
        TypedReturnCode<java.sql.Connection> connection;
        connection = getConnectionBeforeRun(analysis);
        if (!connection.isOk()) {
            this.traceError(connection.getMessage());
            return connection;
        }

        // set it into the evaluator
        eval.setConnection(connection.getObject());
        // use pooled connection
        eval.setPooledConnection(POOLED_CONNECTION);
        return connection;
    }

    /**
     * DOC zshen Comment method "createIndicatorEvaluator".
     * 
     * @param analysis
     * @return
     */
    protected ColumnSetIndicatorEvaluator createIndicatorEvaluator(Analysis analysis) {
        return new ColumnSetIndicatorEvaluator(analysis);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    protected String createSqlStatement(Analysis analysis) {

        // MOD yyi 2011-02-22 17871:delimitefile
        if (isDelimitedFile) {
            return PluginConstant.EMPTY_STRING;
        }
        // ~
        this.cachedAnalysis = analysis;
        StringBuilder sql = new StringBuilder("SELECT ");//$NON-NLS-1$
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        // MOD yyi 2011-02-22 17871:delimitefile
        EList<ModelElement> analysedElements = null;
        for (Indicator indicator : indicators) {
            if (ColumnsetPackage.eINSTANCE.getColumnSetMultiValueIndicator().isSuperTypeOf(indicator.eClass())) {
                ColumnSetMultiValueIndicator colSetMultValIndicator = (ColumnSetMultiValueIndicator) indicator;
                if (analysedElements == null) {
                    analysedElements = colSetMultValIndicator.getAnalyzedColumns();
                } else {
                    analysedElements.addAll(colSetMultValIndicator.getAnalyzedColumns());
                }
            }
        }

        if (analysedElements == null || analysedElements.isEmpty()) {
            setError(Messages.getString("ColumnAnalysisExecutor.CannotCreateSQLStatement",//$NON-NLS-1$
                    analysis.getName()));
            return null;
        }
        // MOD yyi 2011-02-22 17871:delimitefile, indiactor changed
        final Iterator<ModelElement> iterator = analysedElements.iterator();
        while (iterator.hasNext()) { // for (ModelElement modelElement : analysedElements) {
            ModelElement modelElement = iterator.next();
            // --- preconditions
            TdColumn col = SwitchHelpers.COLUMN_SWITCH.doSwitch(modelElement);
            if (col == null) {
                setError(Messages.getString("ColumnAnalysisExecutor.GivenElementIsNotColumn", modelElement)); //$NON-NLS-1$
                return null;
            }
            Classifier owner = col.getOwner();
            if (owner == null) {
                setError(Messages.getString("ColumnAnalysisExecutor.NoOwnerFound", col.getName())); //$NON-NLS-1$
            }
            ColumnSet colSet = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(owner);
            if (colSet == null) {
                setError(Messages.getString("ColumnAnalysisExecutor.NoContainerFound", col.getName()));//$NON-NLS-1$
                return null;
            }
            // else add into select
            // select all the column to be prepare for drill down when user need.
            if (!analysis.getParameters().isStoreData()) {
                sql.append(this.quote(col.getName()));
                // append comma if more columns exist
                if (iterator.hasNext()) {
                    sql.append(',');
                }
            }

        }
        TdColumn firstColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(analysedElements.get(0));
        // select all the column to be prepare for drill down.
        if (analysis.getParameters().isStoreData()) {
            // MOD klliu 2011-06-30 bug 22523 whichever is Table or View,that finds columns should ues columnset
            EObject eContainer = firstColumn.eContainer();
            List<TdColumn> columnList = ColumnSetHelper.getColumns(SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(eContainer));
            // ~
            Iterator<TdColumn> iter = columnList.iterator();
            while (iter.hasNext()) {
                TdColumn column = iter.next();
                sql.append(this.quote(column.getName()));
                // append comma if more columns exist
                if (iter.hasNext()) {
                    sql.append(',');
                }
            }
        }

        // add from clause
        sql.append(dbms().from());
        sql.append(dbms().getQueryColumnSetWithPrefix(firstColumn));

        // add where clause
        // --- get data filter
        ModelElementAnalysisHandler handler = new ModelElementAnalysisHandler();
        handler.setAnalysis(analysis);
        String stringDataFilter = handler.getStringDataFilter();

        sql.append(GenericSQLHandler.WHERE_CLAUSE);

        String sqlStatement = sql.toString();
        sqlStatement = dbms().addWhereToStatement(sqlStatement, stringDataFilter);
        return sqlStatement;
    }

    @Override
    protected boolean check(Analysis analysis) {

        if (!super.check(analysis)) {
            return false;
        } else {
            EList<Indicator> indicators = analysis.getResults().getIndicators();
            for (Indicator indicator : indicators) {
                if (indicator instanceof AllMatchIndicator) {
                    if (!checkAllMatchIndicator((AllMatchIndicator) indicator)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean checkAllMatchIndicator(AllMatchIndicator indicator) {
        EList<RegexpMatchingIndicator> indicators = indicator.getCompositeRegexMatchingIndicators();
        String patternNames = PluginConstant.EMPTY_STRING;
        for (RegexpMatchingIndicator rmi : indicators) {
            if (null == rmi.getRegex()) {

                patternNames += System.getProperty("line.separator") + "\"" + rmi.getName() + "\"";//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            } else if (rmi.getRegex().equals(rmi.getName())) {
                patternNames += System.getProperty("line.separator") + "\"" + rmi.getName() + "\"";//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            }
        }
        if (PluginConstant.EMPTY_STRING != patternNames) {
            traceError(Messages.getString("MultiColumnAnalysisExecutor.checkAllMatchIndicatorForDbType", patternNames)); //$NON-NLS-1$
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#evaluate(org.talend.dataquality.analysis.Analysis,
     * java.sql.Connection, java.lang.String)
     */
    @Override
    protected ReturnCode evaluate(Analysis analysis, Connection connection, String sqlStatement) {
        // no need to implement
        return null;
    }
}
