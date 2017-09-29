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

import org.eclipse.jface.wizard.IWizardPage;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.utils.IndicatorEnumUtils;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisMetadataWizardPage;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class DiscreteDataWizard extends ColumnWizard {

    public DiscreteDataWizard(AnalysisParameter parameter) {
        super(parameter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.analysis.column.ColumnWizard#getPredefinedColumnIndicator()
     */
    @Override
    protected ModelElementIndicator[] getPredefinedColumnIndicator() {
        IndicatorEnum[] allowedEnumes = IndicatorEnumUtils.getForDiscreteDataAnalysis();
        return composePredefinedColumnIndicator(allowedEnumes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.analysis.column.ColumnWizard#addPages()
     */
    @Override
    public void addPages() {
        addPage(new AnalysisMetadataWizardPage());
        // when from the right menu, no need to show select data wizard
        if (parameter.getConnectionRepNode() == null) {
            selectionPage = new NumericalDPSelectionPage();
            addPage(selectionPage);
        }
        for (IWizardPage page : getExtenalPages()) {
            addPage(page);
        }
    }
}
