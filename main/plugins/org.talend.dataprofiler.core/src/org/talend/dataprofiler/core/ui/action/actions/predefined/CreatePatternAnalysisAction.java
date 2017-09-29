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
package org.talend.dataprofiler.core.ui.action.actions.predefined;

import org.eclipse.jface.wizard.WizardDialog;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction;
import org.talend.dataprofiler.core.ui.utils.IndicatorEnumUtils;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dq.analysis.parameters.AnalysisLabelParameter;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class CreatePatternAnalysisAction extends AbstractPredefinedAnalysisAction {

    /**
     * DOC Zqin CreatePatternAnalysisAction constructor comment.
     */
    public CreatePatternAnalysisAction() {
        super(DefaultMessagesImpl.getString("CreatePatternAnalysisAction.PatternAnalysis"), null); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#getPredefinedColumnIndicator()
     */
    @Override
    protected ModelElementIndicator[] getPredefinedColumnIndicator() {
        IndicatorEnum[] allowedEnumes = IndicatorEnumUtils.getForPatternFrequencyAnalysis();
        return composePredefinedColumnIndicator(allowedEnumes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#getPredefinedDialog()
     */
    @Override
    protected WizardDialog getPredefinedDialog() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#isAllowed()
     */
    @Override
    protected boolean isAllowed() {
        if (!RepNodeUtils.isValidSelectionFromSameTable(getSelection().toList())) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#preDo()
     */
    @Override
    protected boolean preDo() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#getCategoryLabel()
     */
    @Override
    protected String getCategoryLabel() {
        return AnalysisLabelParameter.PATTERN_FREQUENCY_ANALYSIS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#needChangeExecuteLanguageToJava()
     */
    @Override
    protected boolean needChangeExecuteLanguageToJava() {
        // TDQ-12349: when the database can not support PatternFrequency, set to java engine automatically
        return !RepNodeUtils.isSupportPatternFrequency(getSelection().toList());
    }
}
