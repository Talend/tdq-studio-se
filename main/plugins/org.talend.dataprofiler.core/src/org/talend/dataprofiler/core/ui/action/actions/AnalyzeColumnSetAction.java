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
import java.util.Iterator;
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
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnSetAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.parameters.PackagesAnalyisParameter;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class AnalyzeColumnSetAction extends Action {

    private List<IRepositoryNode> catalogs = new ArrayList<IRepositoryNode>();

    TreeSelection selection;

    TdColumn[] columns;

    private DBSchemaRepNode schemaNode = null;

    IRepositoryNode nodeColumns;

    boolean needselection = true;

    public AnalyzeColumnSetAction() {
        super(DefaultMessagesImpl.getString("AnalyzeColumnSetAction.Name")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
    }

    public AnalyzeColumnSetAction(TdColumn[] columns) {
        super(DefaultMessagesImpl.getString("AnalyzeColumnSetAction.Name")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
        needselection = false;
        this.columns = columns;
    }

    public AnalyzeColumnSetAction(IRepositoryNode columns) {
        super(DefaultMessagesImpl.getString("AnalyzeColumnSetAction.Name")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
        needselection = false;
        this.nodeColumns = columns;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        PackagesAnalyisParameter packaFilterParameter = new PackagesAnalyisParameter();

        if (nodeColumns != null) {
            DQRepositoryNode tFolder = (DQRepositoryNode) nodeColumns.getParent();
            if (tFolder != null) {
                IRepositoryNode node = tFolder.getParent();

                if (node instanceof DBCatalogRepNode) {
                    IRepositoryNode connNode = ((DBCatalogRepNode) node).getParent();
                    packaFilterParameter.setConnectionRepNode(connNode);
                    catalogs.add(node);
                    packaFilterParameter.setPackages(catalogs);
                } else if (node instanceof DBSchemaRepNode) {
                    schemaNode = (DBSchemaRepNode) node;
                    RepositoryNode parent = schemaNode.getParent();
                    if (parent instanceof DBCatalogRepNode) {
                        catalogs.add(parent);
                        packaFilterParameter.setConnectionRepNode(parent);
                    } else {
                        catalogs.add(schemaNode);
                        packaFilterParameter.setConnectionRepNode(schemaNode);
                    }
                    packaFilterParameter.setPackages(catalogs);
                }

            }
        }

        if (needselection) {
            IRepositoryNode firstElement = (RepositoryNode) this.selection.getFirstElement();
            IRepositoryViewObject viewObject = firstElement.getObject();
            Item item = viewObject.getProperty().getItem();

            ConnectionItem connectionItem = (ConnectionItem) item;
            Connection connection = connectionItem.getConnection();

            IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(connection);
            packaFilterParameter.setConnectionRepNode(repositoryNode);
        }

        if (opencolumnSetAnalysisDialog(packaFilterParameter) == Window.OK) {
            AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (editor != null) {
                ColumnSetAnalysisDetailsPage page = (ColumnSetAnalysisDetailsPage) editor.getMasterPage();
                if (this.needselection && !this.selection.isEmpty()) {
                    IRepositoryNode[] nodeArray = new IRepositoryNode[selection.size()];
                    Iterator it = this.selection.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        nodeArray[i] = (IRepositoryNode) it.next();
                        i++;
                    }
                    page.setTreeViewInput(nodeArray);
                } else if (!this.needselection && null != this.nodeColumns) {
                    List<IRepositoryNode> column = new ArrayList<IRepositoryNode>();
                    for (IRepositoryNode columnFolder : nodeColumns.getChildren()) {
                        if (columnFolder instanceof DBColumnFolderRepNode) {
                            column.addAll(columnFolder.getChildren());
                        }
                    }
                    page.setTreeViewInput(column.toArray());
                }
                page.doSave(null);
            }
        }
    }

    public void setColumnSelection(TreeSelection selection) {
        this.selection = selection;
        // setEnabled(selection.toList().size() > 1);
    }

    private int opencolumnSetAnalysisDialog(PackagesAnalyisParameter packaFilterParameter) {
        Wizard wizard = WizardFactory.createAnalysisWizard(AnalysisType.COLUMN_SET, packaFilterParameter);
        wizard.setForcePreviousAndNextButtons(!this.needselection);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(500, 340);

        return dialog.open();
    }

}
