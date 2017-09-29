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
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dq.indicators.AbstractSchemaEvaluator;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public abstract class AbstactSchemaAnalysisExecutor extends AnalysisExecutor {

    private static Logger log = Logger.getLogger(AbstactSchemaAnalysisExecutor.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    protected String createSqlStatement(Analysis analysis) {
        return PluginConstant.EMPTY_STRING;
    }

    /**
     * DOC scorreia Comment method "runAnalysisLow".
     * 
     * @param analysis
     * @param sqlStatement
     * @param eval
     * @return
     */
    protected ReturnCode runAnalysisLow(Analysis analysis, String sqlStatement, AbstractSchemaEvaluator<?> eval,
            java.sql.Connection connection) {
        // set it into the evaluator
        eval.setConnection(connection);
        // use pooled connection
        eval.setPooledConnection(POOLED_CONNECTION);

        // set filters
        String tablePattern = getTablePattern(analysis.getParameters());
        eval.setTablePattern(tablePattern);
        String viewPattern = getViewPattern(analysis.getParameters());
        eval.setViewPattern(viewPattern);

        // when to close connection
        boolean closeAtTheEnd = true;
        return eval.evaluateIndicators(sqlStatement, closeAtTheEnd);
    }

    /**
     * DOC scorreia Comment method "getTablePattern".
     * 
     * @param parameters
     * @return
     */
    private String getTablePattern(AnalysisParameters parameters) {
        if (parameters == null) {
            return null;
        }
        EList<Domain> dataFilters = parameters.getDataFilter();
        return DomainHelper.getTablePattern(dataFilters);
    }

    private String getViewPattern(AnalysisParameters parameters) {
        if (parameters == null) {
            return null;
        }
        EList<Domain> dataFilters = parameters.getDataFilter();
        return DomainHelper.getViewPattern(dataFilters);
    }
}
