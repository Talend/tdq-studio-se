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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.CompositeIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool;
import org.talend.dq.helper.AnalysisExecutorHelper;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class ColumnAnalysisSqlParallelExecutor extends ColumnAnalysisSqlExecutor {

    private static Logger log = Logger.getLogger(ColumnAnalysisSqlParallelExecutor.class);

    protected Connection connection;

    protected Map<ModelElement, List<Indicator>> elementToIndicator;

    protected Indicator indicator;

    private ColumnAnalysisSqlParallelExecutor() {
        super();
    }

    public static ColumnAnalysisSqlParallelExecutor createInstance(ColumnAnalysisSqlExecutor parent) {
        ColumnAnalysisSqlParallelExecutor inst = new ColumnAnalysisSqlParallelExecutor();
        if (parent != null) {
            inst.setError(parent.getErrorMessage());
            // MOD scorreia 2009-08-20 line commented out: use protected method dbms() instead
            // inst.dbmsLanguage = parent.dbmsLanguage;
            inst.cachedAnalysis = parent.cachedAnalysis;
            inst.schemata = parent.schemata;
            inst.setMonitor(parent.getMonitor());
        }
        return inst;
    }

    public static ColumnAnalysisSqlParallelExecutor createInstance(ColumnAnalysisSqlExecutor parent, Connection connection,
            Map<ModelElement, List<Indicator>> elementToIndicator, Indicator indicator) {
        ColumnAnalysisSqlParallelExecutor inst = createInstance(parent);
        if (inst != null) {
            inst.connection = connection;
            inst.elementToIndicator = elementToIndicator;
            inst.indicator = indicator;
        }
        return inst;
    }

    /**
     * run analysis when SqlParallelExecutor.
     * 
     * @return true if successfull , false otherwise.
     */
    public Boolean run() {
        Expression query = null;
        try {
            if (!continueRun()) {
                return Boolean.FALSE;
            }
            // skip composite indicators that do not require a sql execution
            if (indicator instanceof CompositeIndicator) {
                // options of composite indicators are handled elsewhere
                return Boolean.TRUE;
            }

            synchronized (schemata) {
                // set the connection's catalog
                if (belongToSameSchemata(indicator.getAnalyzedElement())) {
                    if (needChangeCatalog(connection)) {
                        String catalogName = getCatalogOrSchemaName(indicator.getAnalyzedElement());
                        if (catalogName != null) { // check whether null argument can be given
                            changeCatalog(catalogName, connection);
                        }
                    }
                }
            }

            // add mapping of analyzed elements to their indicators
            synchronized (elementToIndicator) {
                addElements2IndicatorsMapping(elementToIndicator, indicator);
            }

            query = dbms().getInstantiatedExpression(indicator);
            if (query == null) {
                traceError(getErrorMessageForQuery(query));
                return Boolean.FALSE;
            }

            try {
                boolean execStatus = executeQuery(indicator, connection, query.getBody());
                if (!execStatus) {
                    traceError(getErrorMessageForQuery(query));
                    return Boolean.FALSE;
                }
            } catch (SQLException e) {
                log.error(e, e);
                traceError(getErrorMessageForQuery(query));
                return Boolean.FALSE;
            }
            // set computation done
            indicator.setComputed(true);

        } finally {
            // return the connection after run
            if (POOLED_CONNECTION) {
                TdqAnalysisConnectionPool.returnPooledConnection(cachedAnalysis, connection);
            }
        }
        return Boolean.TRUE;
    }

    private String getErrorMessageForQuery(Expression query) {
        if (query == null) {
            return Messages.getString(
                    "ColumnAnalysisSqlParallelExecutor.QueryIsNull", AnalysisExecutorHelper.getIndicatorName(indicator)); //$NON-NLS-1$
        } else {
            return Messages.getString(
                    "ColumnAnalysisSqlParallelExecutor.QueryNotExecute", AnalysisExecutorHelper.getIndicatorName(indicator)) //$NON-NLS-1$
                    + PluginConstant.SPACE_STRING
                    + Messages.getString("ColumnAnalysisSqlParallelExecutor.SQLQuery", query.getBody()); //$NON-NLS-1$
        }
    }
}
