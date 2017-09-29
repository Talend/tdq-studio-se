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
package org.talend.dataprofiler.core.ui.action.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.indicator.ImportIndicatorDefinitionWizard;
import org.talend.dq.helper.ProxyRepositoryManager;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ImportIndicatorDefinitionAction extends Action {

    public ImportIndicatorDefinitionAction() {
        super(DefaultMessagesImpl.getString("ImportIndicatorDefinitionAction.ImportIndicators")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.IMPORT));
    }

    @Override
    public void run() {
        ImportIndicatorDefinitionWizard wizard = new ImportIndicatorDefinitionWizard();
        WizardDialog dialog = new WizardDialog(null, wizard);
        wizard.setWindowTitle(getText());
        if (WizardDialog.OK == dialog.open())
            ProxyRepositoryManager.getInstance().save();
    }
}
