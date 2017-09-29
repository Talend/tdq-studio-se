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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class AnalyzeColumnAction extends Action {

    TreeSelection selection;

    public AnalyzeColumnAction() {
        super(DefaultMessagesImpl.getString("AnalyzeColumnAction.analyze")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {

        if (openStandardAnalysisDialog(AnalysisType.MULTIPLE_COLUMN) == Window.OK) {
            AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (editor != null) {
                ColumnAnalysisDetailsPage page = (ColumnAnalysisDetailsPage) editor.getMasterPage();
                if (!this.selection.isEmpty()) {
                    List<RepositoryNode> nodeList = new ArrayList<RepositoryNode>();
                    Object[] objs = this.selection.toArray();
                    for (Object obj : objs) {
                        if (obj instanceof RepositoryNode) {
                            nodeList.add((RepositoryNode) obj);
                        }
                    }
                    if (page != null) {
                        page.setTreeViewInput(objs);
                        page.doSave(null);
                    }
                }
            }
        }
    }

    public void setColumnSelection(TreeSelection selection) {
        this.selection = selection;
    }

    private int openStandardAnalysisDialog(AnalysisType type) {
        // MOD klliu 2011-02-16 Modify this action for distincting CreateNewAnalysisAction feature 15387
        IRepositoryNode firstElement = (RepositoryNode) this.selection.getFirstElement();
        IRepositoryViewObject viewObject = firstElement.getObject();
        Item item = viewObject.getProperty().getItem();

        ConnectionItem connectionItem = (ConnectionItem) item;
        Connection connection = connectionItem.getConnection();

        IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setConnectionRepNode(repositoryNode);
        Wizard wizard = WizardFactory.createAnalysisWizard(type, parameter);
        wizard.setForcePreviousAndNextButtons(true);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(500, 340);

        return dialog.open();
    }
}
