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
import org.talend.core.model.metadata.MetadataXmlElementType;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by sizhaoliu on 18 oct. 2013 Detailled comment
 * 
 */
public class MatchAnalysisAction extends Action {

    private TreeSelection selection;

    boolean needselection = true;

    public MatchAnalysisAction() {
        super(DefaultMessagesImpl.getString("MatchAnalysisAction.createMatchAnalysis")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
    }

    protected IRepositoryNode[] getColumns() {
        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        Object firstElement = getSelection().getFirstElement();
        if (firstElement instanceof IRepositoryNode) {
            IRepositoryNode repNode = (IRepositoryNode) firstElement;
            IRepositoryViewObject repViewObject = repNode.getObject();
            if (repViewObject instanceof MetadataColumnRepositoryObject || repViewObject instanceof MetadataXmlElementType) {
                IRepositoryNode[] column = new IRepositoryNode[getSelection().size()];
                for (int i = 0; i < getSelection().size(); i++) {
                    column[i] = (IRepositoryNode) getSelection().toArray()[i];
                }
                return column;
            } else if (repViewObject instanceof MetadataTableRepositoryObject) {
                Object[] selections = getSelection().toArray();
                for (Object currentObj : selections) {
                    IRepositoryNode columnSetNode = (IRepositoryNode) currentObj;
                    List<IRepositoryNode> children = columnSetNode.getChildren();
                    if (children.size() > 0) {
                        list.addAll(children.get(0).getChildren());
                    }
                }
                return list.toArray(new IRepositoryNode[list.size()]);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {

        PackagesAnalyisParameter packaFilterParameter = new PackagesAnalyisParameter();

        if (needselection) {
            IRepositoryNode firstElement = (RepositoryNode) this.selection.getFirstElement();
            IRepositoryViewObject viewObject = firstElement.getObject();
            Item item = viewObject.getProperty().getItem();

            ConnectionItem connectionItem = (ConnectionItem) item;
            Connection connection = connectionItem.getConnection();

            IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
            packaFilterParameter.setConnectionRepNode(repositoryNode);
        }

        if (openMatchAnalysisDialog(packaFilterParameter) == Window.OK) {
            AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (editor != null) {
                MatchAnalysisDetailsPage page = (MatchAnalysisDetailsPage) editor.getMasterPage();
                IRepositoryNode[] columns = getColumns();
                if (this.needselection && columns.length > 0) {
                    page.updateAnalyzeDataLabel(columns[0]);
                    page.setSelectedNodes(columns);
                }
                page.doSave(null);
            }
        }
    }

    public void setColumnSelection(TreeSelection selection) {
        this.setSelection(selection);
        // setEnabled(selection.toList().size() > 1);
    }

    /**
     * Getter for selection.
     * 
     * @return the selection
     */
    public TreeSelection getSelection() {
        return selection;
    }

    /**
     * Sets the selection.
     * 
     * @param selection the selection to set
     */
    public void setSelection(TreeSelection selection) {
        this.selection = selection;
    }

    private int openMatchAnalysisDialog(PackagesAnalyisParameter packaFilterParameter) {
        Wizard wizard = WizardFactory.createAnalysisWizard(AnalysisType.MATCH_ANALYSIS, packaFilterParameter);
        wizard.setForcePreviousAndNextButtons(!this.needselection);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(500, 340);

        return dialog.open();
    }

}
