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
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class CreateSimpleAnalysisAction extends AbstractPredefinedAnalysisAction {

    public CreateSimpleAnalysisAction() {
        super(DefaultMessagesImpl.getString("CreateSimpleAnalysisAction.simpleAnalysis"), null); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#getPredefinedColumnIndicator()
     */
    @Override
    protected ModelElementIndicator[] getPredefinedColumnIndicator() {
        IndicatorEnum[] allowedEnumes = IndicatorEnumUtils.getForSimpleAnalysis();
        return composePredefinedColumnIndicator(allowedEnumes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#isAllowed()
     */
    @Override
    protected boolean isAllowed() {
        for (Object obj : getSelection().toList()) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                if (node.hasChildren()) {
                    return false;
                }
            } else {
                return false;
            }
        }
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

    @Override
    protected WizardDialog getPredefinedDialog() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractPredefinedAnalysisAction#getCategoryLabel()
     */
    @Override
    protected String getCategoryLabel() {
        return null;
    }
}
