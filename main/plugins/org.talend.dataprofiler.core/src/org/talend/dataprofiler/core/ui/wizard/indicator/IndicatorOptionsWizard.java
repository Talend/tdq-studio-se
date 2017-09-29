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
package org.talend.dataprofiler.core.ui.wizard.indicator;

import org.eclipse.jface.wizard.Wizard;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class IndicatorOptionsWizard extends Wizard {

    private boolean isDirty;

    private IndicatorUnit indicatorUnit;

    private DynamicIndicatorOptionsPage indicatorPage;

    /**
     * DOC zqin IndicatorOptionsWizard constructor comment.
     */
    public IndicatorOptionsWizard(IndicatorUnit indicatorUnit) {
        setWindowTitle(DefaultMessagesImpl.getString("IndicatorOptionsWizard.indicator")); //$NON-NLS-1$
        this.indicatorUnit = indicatorUnit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {

        if (indicatorPage.getValidFroms() != null) {
            for (AbstractIndicatorForm form : indicatorPage.getValidFroms()) {
                boolean isOk = form.performFinish();
                if (!isOk) {
                    return false;
                }
            }
        }

        isDirty = true;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        indicatorPage = new DynamicIndicatorOptionsPage(indicatorUnit);
        addPage(indicatorPage);
    }

    /**
     * Getter for isDirty.
     * 
     * @return the isDirty
     */
    public boolean isDirty() {
        return this.isDirty;
    }
}
