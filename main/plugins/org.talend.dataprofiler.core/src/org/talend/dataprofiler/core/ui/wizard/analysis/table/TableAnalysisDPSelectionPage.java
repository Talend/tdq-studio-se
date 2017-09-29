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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdTable;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.analysis.AnalysisDPSelectionPage;
import org.talend.dq.analysis.parameters.NamedColumnSetAnalysisParameter;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
@Deprecated
public class TableAnalysisDPSelectionPage extends AnalysisDPSelectionPage {

    private static String newAnaStr = DefaultMessagesImpl.getString("TableAnalysisPageStep0.newAnalysis"); //$NON-NLS-1$

    private static String chooseConnStr = DefaultMessagesImpl.getString("TableAnalysisPageStep0.chooseTable"); //$NON-NLS-1$

    private static String connsStr = DefaultMessagesImpl.getString("TableAnalysisPageStep0.tables"); //$NON-NLS-1$

    public Connection connection = null;

    public TableAnalysisDPSelectionPage() {
        super(newAnaStr, chooseConnStr, connsStr, new TableContentProvider(), true);
    }

    @Override
    protected void addListeners() {
        addListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
                if (object instanceof NamedColumnSet) {
                    advanceToNextPageOrFinish();
                }
            }

        });
        addListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                // Connection oldTdDataProvider = null;
                NamedColumnSetAnalysisParameter paraneter = (NamedColumnSetAnalysisParameter) getConnectionParams();
                List tempList = ((IStructuredSelection) event.getSelection()).toList();
                List<NamedColumnSet> setList = new ArrayList<NamedColumnSet>();
                for (Object object : tempList) {
                    if (object instanceof DBTableRepNode) {
                        DBTableRepNode tableNode = (DBTableRepNode) object;
                        // MOD klliu if table node is not initialized , then init that . bug 20097 2011-03-31
                        if (!tableNode.isInitialized()) {
                            tableNode.getChildren().get(0).getChildren();
                        }
                        // ~
                        setList.add(tableNode.getTdTable());
                    }
                    // if (object instanceof NamedColumnSet) {
                    // NamedColumnSet set = (NamedColumnSet) object;
                    // Connection tdProvider = ConnectionHelper.getTdDataProvider(TableHelper
                    // .getParentCatalogOrSchema(set));
                    // oldTdDataProvider = oldTdDataProvider == null ? tdProvider : oldTdDataProvider;
                    // if (oldTdDataProvider != null && !oldTdDataProvider.equals(tdProvider)) {
                    // MessageUI.openWarning(DefaultMessagesImpl
                    //                                    .getString("TableAnalysisDPSelectionPage.TableSelectWarning")); //$NON-NLS-1$
                    // } else if (tdProvider != null && paraneter != null) {
                    // setList.add(set);
                    // paraneter.setTdDataProvider(oldTdDataProvider);
                    // }
                    // }
                }
                if (setList.size() > 0 && paraneter != null) {
                    paraneter.setNamedColumnSets(setList.toArray(new NamedColumnSet[setList.size()]));
                    DBTableRepNode recursiveFind = (DBTableRepNode) RepositoryNodeHelper.recursiveFind((TdTable) setList.get(0));
                    RepositoryNode parent = recursiveFind.getParent().getParent();
                    // MOD qiongli 2011-3-16 bug 19475
                    RepositoryNode catalogNode = parent;
                    if (parent instanceof DBCatalogRepNode) {
                        parent = parent.getParent();
                    } else if (parent instanceof DBSchemaRepNode) {
                        parent = parent.getParent();
                        if (parent instanceof DBCatalogRepNode) {
                            catalogNode = parent;
                            parent = parent.getParent();
                        }
                    }
                    // TdTableRepositoryObject tableViewObject = (TdTableRepositoryObject) recursiveFind.getObject();
                    // IRepositoryViewObject viewObject = parent.getObject();
                    DBConnectionRepNode connNode = (DBConnectionRepNode) parent;
                    paraneter.setConnectionRepNode(connNode);
                    Connection connection = ConnectionHelper.getConnection((TdTable) setList.get(0));
                    paraneter.setTdDataProvider(connection);
                    List<IRepositoryNode> packagesNode = new ArrayList<IRepositoryNode>();
                    packagesNode.add(catalogNode);
                    paraneter.setPackages(packagesNode);
                    setPageComplete(true);
                } else {
                    setPageComplete(false);
                }
            }
        });
    }
}
