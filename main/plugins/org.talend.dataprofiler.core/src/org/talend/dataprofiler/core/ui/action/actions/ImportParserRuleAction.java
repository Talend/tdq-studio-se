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
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.parserrule.ImportParserRuleWizard;
import org.talend.dq.helper.ProxyRepositoryManager;


/**
 * DOC klliu class global comment. Detailled comment
 */
public class ImportParserRuleAction extends Action {

    protected static Logger log = Logger.getLogger(ImportParserRuleAction.class);

    private IFolder folder;

    public ImportParserRuleAction(IFolder folder) {
        setText(DefaultMessagesImpl.getString("ImportParserRuleAction.ImportExchange")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.IMPORT));
        this.folder = folder;
    }


    @Override
    public void run() {
        ImportParserRuleWizard wizard = new ImportParserRuleWizard(folder);
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
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
