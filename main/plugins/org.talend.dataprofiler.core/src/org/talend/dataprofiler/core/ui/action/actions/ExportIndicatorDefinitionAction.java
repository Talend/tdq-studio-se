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

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.indicator.ExportIndicatorDefinitionWizard;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ExportIndicatorDefinitionAction extends Action {

    protected static Logger log = Logger.getLogger(ExportIndicatorDefinitionAction.class);

    public ExportIndicatorDefinitionAction() {
        super(DefaultMessagesImpl.getString("ExportIndicatorDefinitionAction.ExportIndicators")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.EXPORT));
    }

    @Override
    public void run() {
        ExportIndicatorDefinitionWizard wizard = new ExportIndicatorDefinitionWizard();
        WizardDialog dialog = new WizardDialog(null, wizard);
        wizard.setWindowTitle(getText());
        dialog.open();
    }
}
