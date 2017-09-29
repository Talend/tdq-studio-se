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
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.indicator.ImportUDIWizard;
import org.talend.dq.helper.ProxyRepositoryManager;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ImportUDIAction extends Action {

    protected static Logger log = Logger.getLogger(ImportUDIAction.class);

    private IFolder folder;

    public ImportUDIAction(IFolder folder) {
        setText(DefaultMessagesImpl.getString("UserDefinedIndicatorsActionProvider.importUDI")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.IMPORT));
        this.folder = folder;
    }

    @Override
    public void run() {
        ImportUDIWizard wizard = new ImportUDIWizard(folder);
        WizardDialog dialog = new WizardDialog(null, wizard);
        wizard.setWindowTitle(getText());
        if (WizardDialog.OK == dialog.open()) {
            try {
                ProxyRepositoryManager.getInstance().save();
                folder.refreshLocal(IResource.DEPTH_INFINITE, null);
            } catch (CoreException e) {
                log.error(e, e);
            }
        }
    }
}
