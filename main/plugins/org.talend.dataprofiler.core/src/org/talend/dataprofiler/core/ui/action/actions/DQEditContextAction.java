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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.metadata.managment.ui.wizard.context.ContextWizard;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * 
 * @author qiongli
 * Edit Or Read Context(reuse DI wizard)
 *
 */
public class DQEditContextAction extends AContextualAction {

    private RepositoryNode node;

    private boolean isReadOnly = false;

    /**
     * 
     * @param node current select node
     * @param isReadOnly
     */
    public DQEditContextAction(RepositoryNode node, boolean isReadOnly) {
        super();
        this.node = node;
        this.isReadOnly = isReadOnly;
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.CONTEXT));
        if (isReadOnly) {
            setText(DefaultMessagesImpl.getString("DQCreateContextAction.readContext"));
        } else {
            setText(DefaultMessagesImpl.getString("DQCreateContextAction.editContext"));
        }
    }

    @Override
    protected void doRun() {
        ContextWizard contextWizard = new ContextWizard(PlatformUI.getWorkbench(), false, node, isReadOnly);
        WizardDialog dlg = new WizardDialog(Display.getCurrent().getActiveShell(), contextWizard);
        if (Window.OK == dlg.open()) {
            CorePlugin.getDefault().refreshDQView(node.getParent());
        }
    }

    @Override
    protected ISelection getRepositorySelection() {
        return getSelection();
    }

    @Override
    public ISelection getSelection() {
        ISelection selection;
        if (node == null) {
            // select by UI(tree)
            DQRespositoryView findView = CorePlugin.getDefault().getRepositoryView();
            selection = findView.getCommonViewer().getSelection();
        } else {
            // new instance of selection for dependency modeleEelemnt.
            selection = new StructuredSelection(node);
        }
        return selection;
    }

    public void init(TreeViewer viewer, IStructuredSelection selection) {
        // TODO Auto-generated method stub

    }

}
