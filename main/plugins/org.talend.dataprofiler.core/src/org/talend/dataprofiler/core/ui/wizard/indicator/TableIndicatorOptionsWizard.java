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
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.AbstractIndicatorForm;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TableIndicatorOptionsWizard extends Wizard {

    private boolean isDirty;

    private TableIndicatorUnit indicatorUnit;

    private DynamicTableIndicatorOptionsPage indicatorPage;

    public TableIndicatorOptionsWizard(TableIndicatorUnit indicatorUnit) {
        setWindowTitle(DefaultMessagesImpl.getString("TableIndicatorOptionsWizard.indicator")); //$NON-NLS-1$
        this.indicatorUnit = indicatorUnit;
    }

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

    @Override
    public void addPages() {
        indicatorPage = new DynamicTableIndicatorOptionsPage(indicatorUnit);
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
