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

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.SchemaEvaluator;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class SchemaAnalysisExecutor extends AbstactSchemaAnalysisExecutor {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.AnalysisExecutor#runAnalysis(org.talend.dataquality.analysis.Analysis,
     * java.lang.String)
     */
    @Override
    protected ReturnCode evaluate(Analysis analysis, java.sql.Connection connection, String sqlStatement) {
        SchemaEvaluator eval = new SchemaEvaluator();
        // MOD xqliu 2009-02-09 bug 6237
        eval.setMonitor(getMonitor());

        // // --- add indicators
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            ModelElement analyzedElement = indicator.getAnalyzedElement();
            if (analyzedElement == null) {
                continue;
            }

            Schema cat = SwitchHelpers.SCHEMA_SWITCH.doSwitch(analyzedElement);
            if (cat == null) {
                continue;
            }
            eval.storeIndicator(cat, indicator);
            // ADDED rli 2008-07-10 fixed for the SchemaIndicator will increased after connection analysis running.
            indicator.reset();
        }
        if (getMonitor() != null) {
            getMonitor().worked(compIndicatorsWorked);
        }
        ReturnCode retCode = runAnalysisLow(analysis, sqlStatement, eval, connection);
        return retCode;
    }

}
