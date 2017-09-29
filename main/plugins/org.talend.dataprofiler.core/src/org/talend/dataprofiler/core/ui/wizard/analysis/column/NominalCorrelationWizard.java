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
package org.talend.dataprofiler.core.ui.wizard.analysis.column;

import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dq.analysis.parameters.AnalysisParameter;

/**
 * DOC BZhou class global comment. Detailled comment
 */
public class NominalCorrelationWizard extends ColumnSetWizard {

    /**
     * DOC BZhou ColumnNominalWizard constructor comment.
     * 
     * @param parameter
     */
    public NominalCorrelationWizard(AnalysisParameter parameter) {
        super(parameter);
        setIndicator(ColumnsetFactory.eINSTANCE.createWeakCorrelationIndicator());
    }
}
