// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.writer.impl.AnalysisWriter;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.sugars.ReturnCode;

import orgomg.cwm.foundation.softwaredeployment.DataProvider;

/**
 * @author scorreia
 *
 * A class to get the correct executor for a given analysis.
 */
public final class AnalysisExecutorSelector {

    private static Logger log = Logger.getLogger(AnalysisExecutorSelector.class);

    private AnalysisExecutorSelector() {
    }

    /**
     * Method "getAnalysisExecutor".
     *
     * @param analysis the analysis to be run by the executor.
     * @return the appropriate executor of the analysis or null when no appropriate executor has been found.
     */
    public static IAnalysisExecutor getAnalysisExecutor(Analysis analysis) {
        assert analysis != null;
        AnalysisType analysisType = AnalysisHelper.getAnalysisType(analysis);
        if (analysisType == null) {
            log.error(Messages.getString("AnalysisExecutorSelector.ANALYSISTYPEISNOTSET", analysis.getName())); //$NON-NLS-1$
            return null;
        }
        ExecutionLanguage executionEngine = AnalysisHelper.getExecutionEngine(analysis);
        IAnalysisExecutor exec = null;
        switch (analysisType) {
        case MULTIPLE_COLUMN:
            exec = getColumnAnalysisExecutor(analysis, executionEngine);
            break;
        case CONNECTION:
            exec = new ConnectionAnalysisExecutor();
            break;
        case SCHEMA:
            exec = new SchemaAnalysisExecutor();
            break;
        case CATALOG:
            exec = new CatalogAnalysisExecutor();
            break;
        case COLUMNS_COMPARISON:
            exec = new RowMatchingAnalysisExecutor();
            break;
        case COLUMN_CORRELATION:
            exec = new MultiColumnAnalysisExecutor();
            break;
        case COLUMN_SET:
            // MOD yyi 2011-02-22 17871:delimitefile
            exec = getColumnSetAnalysisExecutor(analysis, executionEngine);
            break;
        case TABLE:
            exec = new TableAnalysisSqlExecutor();
            break;
        case TABLE_FUNCTIONAL_DEPENDENCY:
            exec = new FunctionalDependencyExecutor();
            break;
        case MATCH_ANALYSIS:
            exec = new MatchAnalysisExecutor();
            break;
        default:
            // this should not happen. This executor has not been tested for a long time.
            exec = null;
        }
        return exec;
    }

    /**
     * return Column or TdXMLElement analysis executor. ADD xqliu bug 10238 2009-12-24
     *
     * @param analysis
     * @param executionEngine
     * @return
     */
    private static AnalysisExecutor getColumnAnalysisExecutor(Analysis analysis, ExecutionLanguage executionEngine) {
        // MOD qiongli 2010-11-9 feature 16796
        boolean isDelimitedFile = ConnectionUtils.isDelimitedFileConnection((DataProvider) analysis.getContext().getConnection());
        boolean isSQLEngine = ExecutionLanguage.SQL.equals(executionEngine);
        boolean isUseSampleData = AnalysisHelper.isUseSampleData(analysis);
        if (isDelimitedFile) {
            if (isUseSampleData) {
                return new DelimitedFileAnalysisExecutorWithSampleData();
            } else {
                return new DelimitedFileAnalysisExecutor();
            }
        } else {
            if (isUseSampleData) {
                return new ColumnAnalysisExecutorWithSampleData();
            } else {
                return isSQLEngine ? new ColumnAnalysisSqlExecutor() : new ColumnAnalysisExecutor();
            }
        }
    }

    /**
     * yyi 2011-02-22 17871:delimitefile.
     *
     * @param analysis
     * @param executionEngine
     * @return
     */
    private static AnalysisExecutor getColumnSetAnalysisExecutor(Analysis analysis, ExecutionLanguage executionEngine) {
        boolean isDelimitedFile = ConnectionUtils.isDelimitedFileConnection((DataProvider) analysis.getContext().getConnection());
        boolean isSQLEngine = ExecutionLanguage.SQL.equals(executionEngine);
        boolean isUseSampleData = AnalysisHelper.isUseSampleData(analysis);
        if (isUseSampleData) {
            return new ColumnSetAnalysisExecutorWithSampleData(isDelimitedFile, false);
        } else {
            return isSQLEngine ? new MultiColumnAnalysisExecutor()
                    : new ColumnSetAnalysisExecutor(isDelimitedFile, false);
        }
    }

    /**
     * Method "executeAnalysis".
     *
     * @param analysis an analysis to be run
     * @return a return code with an error message when the analysis failed to run.
     */
    public static ReturnCode executeAnalysis(Analysis analysis) {
        return executeAnalysis(analysis, null);
    }

    public static ReturnCode executeAnalysis(final TDQAnalysisItem analysisItem, final IProgressMonitor monitor) {

        IAnalysisExecutor analysisExecutor = getAnalysisExecutor(analysisItem.getAnalysis());
        if (analysisExecutor != null) {
            // MOD xqliu 2009-02-09 bug 6237
            analysisExecutor.setMonitor(monitor);
            ReturnCode execute = analysisExecutor.execute(analysisItem.getAnalysis());
            // save analysis.
            if (Platform.isRunning()) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        if (monitor != null && !monitor.isCanceled()) {
                            monitor.subTask(Messages.getString("AnalysisExecutorSelector.SavingAnalysisTask")); //$NON-NLS-1$
                            AnalysisWriter writer = ElementWriterFactory.getInstance().createAnalysisWrite();
                            writer.save(analysisItem, Boolean.FALSE);
                            monitor.worked(10);
                        }
                    }
                });
            }

            return execute;
        }
        // else
        return new ReturnCode(Messages.getString(
                "AnalysisExecutorSelector.NotFindHowExecute", analysisItem.getAnalysis().getName()), false); //$NON-NLS-1$

    }

    /**
     *
     * DOC xqliu Comment method "executeAnalysis".
     *
     * @param analysis
     * @param monitor
     * @return
     * @deprecated use {@link #executeAnalysis(TDQAnalysisItem, IProgressMonitor)} instead.
     */
    @Deprecated
    public static ReturnCode executeAnalysis(final Analysis analysis, IProgressMonitor monitor) {
        IAnalysisExecutor analysisExecutor = getAnalysisExecutor(analysis);
        if (analysisExecutor != null) {
            // MOD xqliu 2009-02-09 bug 6237
            analysisExecutor.setMonitor(monitor);
            ReturnCode execute = analysisExecutor.execute(analysis);

            // save analysis.
            if (Platform.isRunning()) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        AnaResourceFileHelper.getInstance().save(analysis);
                    }
                });
            }

            return execute;
        }
        // else
        return new ReturnCode(Messages.getString("AnalysisExecutorSelector.NotFindHowExecute", analysis.getName()), false); //$NON-NLS-1$
    }
}
