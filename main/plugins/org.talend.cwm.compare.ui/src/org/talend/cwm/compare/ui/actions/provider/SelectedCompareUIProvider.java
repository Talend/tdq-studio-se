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
package org.talend.cwm.compare.ui.actions.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.cwm.compare.i18n.Messages;
import org.talend.cwm.compare.ui.actions.SelectedComparisonAction;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC mzhao class global comment. Compare selected model elements.
 */
public class SelectedCompareUIProvider extends AbstractCommonActionProvider {

    private static final String COMPARISON_MENUTEXT = Messages.getString("SelectedCompareUIProvider.Comparison"); //$NON-NLS-1$

    private SelectedComparisonAction selectionCompareAction = null;

    public SelectedCompareUIProvider() {
        selectionCompareAction = new SelectedComparisonAction(COMPARISON_MENUTEXT);
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        // TDQ-9394 hide all "Reload" menu.
        if (!isShowMenu()) {
            return;
        }
        if (isShowMenu()) {
            return;
        }
        Iterator<?> iter = ((TreeSelection) this.getContext().getSelection()).iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (!(obj instanceof RepositoryNode)) {
                return;
            }
        }
        TreeSelection treeSelection = (TreeSelection) getContext().getSelection();
        if (treeSelection == null) {
            return;
        }

        Object firstElement = treeSelection.getFirstElement();
        RepositoryNode rNode = (RepositoryNode) firstElement;
        IFolder folder = WorkbenchUtils.getFolder(rNode);
        IFolder metadataFolder = ResourceManager.getMetadataFolder();
        if (!folder.getFullPath().toOSString().startsWith(metadataFolder.getFullPath().toOSString())) {
            return;
        }

        Object[] selectedObj = treeSelection.toArray();
        if (selectedObj.length < 2) {
            return;
        }

        List<Object> objects = new ArrayList<Object>();
        for (Object obj : selectedObj) {
            Connection conn = getConnection(obj);

            if (!MetadataConnectionUtils.isTDQSupportDBTemplate(conn)) {
                return;
            }
            RepositoryNode node = (RepositoryNode) obj;
            if (node instanceof DBConnectionRepNode) {
                Property property = ((DBConnectionRepNode) node).getObject().getProperty();
                DatabaseConnectionItem databaseConnectionItem = (DatabaseConnectionItem) property.getItem();
                Connection databaseConnection = databaseConnectionItem.getConnection();
                objects.add(databaseConnection);
            } else if (node instanceof DBCatalogRepNode) {
                Catalog catalog = ((DBCatalogRepNode) node).getCatalog();
                objects.add(catalog);
            } else if (node instanceof DBSchemaRepNode) {
                Schema schema = ((DBSchemaRepNode) node).getSchema();
                objects.add(schema);
            } else if (node instanceof DBTableRepNode) {
                TdTable tdTable = ((DBTableRepNode) node).getTdTable();
                objects.add(tdTable);
            } else if (node instanceof DBColumnRepNode) {
                TdColumn tdColumn = ((DBColumnRepNode) node).getTdColumn();
                objects.add(tdColumn);
            }
        }

        // MOD yyi 2011-03-22 17871:hide compare action for flat file column
        if (2 == objects.size()) {
            selectionCompareAction.refreshSelectedObj(objects.get(0), objects.get(1));
            menu.add(selectionCompareAction);
        }
    }
}
