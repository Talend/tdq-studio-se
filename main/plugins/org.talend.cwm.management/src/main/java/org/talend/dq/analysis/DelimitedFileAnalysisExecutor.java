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

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.helpers.RowCountIndicatorsAdapter;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.DelimitedFileIndicatorEvaluator;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DelimitedFileAnalysisExecutor extends AnalysisExecutor {

    private Connection dataprovider;

    private static Logger log = Logger.getLogger(ColumnAnalysisExecutor.class);

    @Override
    protected String createSqlStatement(Analysis analysis) {
        return PluginConstant.EMPTY_STRING;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#runAnalysis(org.talend.dataquality.analysis.Analysis,
     * java.lang.String)
     */
    @Override
    protected boolean runAnalysis(Analysis analysis, String sqlStatement) {
        DelimitedFileIndicatorEvaluator eval = createIndicatorEvaluator(analysis);
        DelimitedFileConnection con = (DelimitedFileConnection) analysis.getContext().getConnection();
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        eval.setMonitor(getMonitor());
        RowCountIndicatorsAdapter.getInstance().clear();
        for (Indicator indicator : indicators) {
            assert indicator != null;
            MetadataColumn mColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(indicator.getAnalyzedElement());
            if (mColumn == null) {
                continue;
            }
            String columnName = mColumn.getLabel();
            eval.storeIndicator(columnName, indicator);
        }
        eval.setDelimitedFileconnection(con);
        ReturnCode rc = eval.evaluateIndicators(sqlStatement, true);
        if (!rc.isOk()) {
            log.warn(rc.getMessage());
            setError(rc.getMessage());
        }
        if (getMonitor() != null) {
            getMonitor().worked(compIndicatorsWorked);
        }
        return rc.isOk();
    }

    /**
     * DOC zshen Comment method "createIndicatorEvaluator".
     * 
     * @param analysis
     * @return
     */
    protected DelimitedFileIndicatorEvaluator createIndicatorEvaluator(Analysis analysis) {
        return new DelimitedFileIndicatorEvaluator(analysis);
    }

    @Override
    protected boolean check(final Analysis analysis) {
        if (analysis == null) {
            setError(Messages.getString("ColumnAnalysisExecutor.AnalysisIsNull")); //$NON-NLS-1$
            return false;
        }
        if (!super.check(analysis)) {
            // error message already set in super method.
            return false;
        }

        // --- check that there exists at least on element to analyze
        AnalysisContext context = analysis.getContext();
        if (context.getAnalysedElements().size() == 0) {
            setError(Messages.getString("ColumnAnalysisExecutor.AnalysisHaveAtLeastOneColumn")); //$NON-NLS-1$
            return false;
        }

        return checkAnalyzedElements(analysis, context);
    }

    protected boolean checkAnalyzedElements(final Analysis analysis, AnalysisContext context) {
        ModelElementAnalysisHandler analysisHandler = new ModelElementAnalysisHandler();
        analysisHandler.setAnalysis(analysis);

        for (ModelElement node : context.getAnalysedElements()) {
            MetadataColumn column = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(node);

            // --- Check that each analyzed element has at least one indicator
            if (analysisHandler.getIndicators(column).size() == 0) {
                setError(Messages.getString("ColumnAnalysisExecutor.EachColumnHaveOneIndicator")); //$NON-NLS-1$
                return false;
            }

            // --- get the data provider
            Connection dp = ConnectionHelper.getTdDataProvider(column);
            if (!isAccessWith(dp)) {
                setError(Messages.getString("ColumnAnalysisExecutor.AllColumnsBelongSameConnection", //$NON-NLS-1$
                        column.getName(), dataprovider.getName()));
                return false;
            }
        }
        return true;
    }

    protected boolean isAccessWith(Connection dp) {
        if (dataprovider == null) {
            dataprovider = dp;
            return true;
        }
        return ResourceHelper.areSame(dataprovider, dp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#evaluate(org.talend.dataquality.analysis.Analysis,
     * java.sql.Connection, java.lang.String)
     */
    @Override
    protected ReturnCode evaluate(Analysis analysis, java.sql.Connection connection, String sqlStatement) {
        // no need to implement here
        return null;
    }

}
