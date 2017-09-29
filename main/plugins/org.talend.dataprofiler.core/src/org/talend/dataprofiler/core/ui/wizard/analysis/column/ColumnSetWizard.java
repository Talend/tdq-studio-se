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

import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class ColumnSetWizard extends ColumnWizard {

    public ColumnSetWizard(AnalysisParameter parameter) {
        super(parameter);
        setIndicator(ColumnsetFactory.eINSTANCE.createSimpleStatIndicator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.analysis.column.ColumnWizard#initCWMResourceBuilder()
     */
    @Override
    public ModelElement initCWMResourceBuilder() {
        Indicator indicator = getIndicator();
        if (indicator != null && indicator instanceof ColumnSetMultiValueIndicator) {
            ColumnSetMultiValueIndicator mvIndicator = (ColumnSetMultiValueIndicator) indicator;
            mvIndicator.setRowCountIndicator(IndicatorsFactory.eINSTANCE.createRowCountIndicator());
            mvIndicator.setDistinctCountIndicator(IndicatorsFactory.eINSTANCE.createDistinctCountIndicator());
            mvIndicator.setDuplicateCountIndicator(IndicatorsFactory.eINSTANCE.createDuplicateCountIndicator());
            mvIndicator.setUniqueCountIndicator(IndicatorsFactory.eINSTANCE.createUniqueCountIndicator());
        }
        return super.initCWMResourceBuilder();
    }
}
