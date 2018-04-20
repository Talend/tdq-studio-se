// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.metadata.managment.ui.wizard.context.ContextWizard;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * @author qiongli
 * create a context group wizard(reuse DI wizard)
 *
 */
public class DQCreateContextAction extends Action implements ICheatSheetAction {

    protected RepositoryNode node;

    /**
     * 
     * @param node
     */
    public DQCreateContextAction(RepositoryNode node) {
        this.node = node;
        setImageDescriptor(ImageLib.createAddedIcon(ImageLib.CONTEXT, IDecoration.TOP_RIGHT));
        setText(DefaultMessagesImpl.getString("DQCreateContextAction.createContext"));
    }

    @Override
    public void run() {
        ContextWizard contextWizard = new ContextWizard(PlatformUI.getWorkbench(), true, node, false);
        WizardDialog dlg = new WizardDialog(Display.getCurrent().getActiveShell(), contextWizard);
        dlg.open();
    }

    public void run(String[] params, ICheatSheetManager manager) {
        // TODO Auto-generated method stub

    }

}
