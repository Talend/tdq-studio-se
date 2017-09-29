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
package org.talend.dataprofiler.core.ui.action.provider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.model.MetadataTableWithFilter;
import org.talend.dataprofiler.core.ui.action.actions.predefined.SemanticDiscoveryAction;
import org.talend.dq.nodes.ColumnRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.DFTableRepNode;

public class SemanticDiscoveryActionProvider extends AbstractCommonActionProvider {

    private SemanticDiscoveryAction semanticDiscoveryAction;

    public SemanticDiscoveryActionProvider() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
     */
    @Override
    public void init(ICommonActionExtensionSite site) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        TreeSelection currentSelection = ((TreeSelection) this.getContext().getSelection());

        Object firstElement = currentSelection.getFirstElement();
        boolean selectedMoreThanOne=currentSelection.size()>1;
        
        
        // when the selection is valid, only two possible status: some columns in the same columnset are selected.
        // keep all of columns belong to same one table and create SemanticDiscoveryAction.
        if (selectedMoreThanOne || firstElement instanceof ColumnRepNode) {
            Set<String> currentTableSet = new HashSet<String>();
            MetadataTable createTable = ConnectionFactory.eINSTANCE.createMetadataTable();

            List<String> filterNames = new ArrayList<String>();
            Iterator<Object> columnIterator = currentSelection.iterator();
            while (columnIterator.hasNext()) {
                Object columnNode = columnIterator.next();
                MetadataColumn metadataColumn = null;
                if (DBColumnRepNode.class.isInstance(columnNode)) {
                    metadataColumn = ((DBColumnRepNode) columnNode).getTdColumn();
                    createTable = ColumnHelper.getColumnOwnerAsMetadataTable(metadataColumn);
                } else if (DFColumnRepNode.class.isInstance(columnNode)) {
                    metadataColumn = ((DFColumnRepNode) columnNode).getMetadataColumn();
                    createTable = ColumnHelper.getColumnOwnerAsMetadataTable(metadataColumn);
                } else {
                    // If not all of elements which be selected is columns
                    return;
                }

                currentTableSet.add(ResourceHelper.getUUID(createTable));
                // all of columns should come from same table
                if (currentTableSet.size() > 1) {
                    return;
                }
                filterNames.add(metadataColumn.getName());
            }
            MetadataTable metadataTableWithFilter = new MetadataTableWithFilter(filterNames, createTable);
            semanticDiscoveryAction = new SemanticDiscoveryAction(metadataTableWithFilter);
        }else{
            // otherwise the selection is valid, only two possible status: only one columnset is select.
            if (firstElement instanceof DBTableRepNode) {
                DBTableRepNode node = (DBTableRepNode) firstElement;
                semanticDiscoveryAction = new SemanticDiscoveryAction(node.getTdTable());
            } else if (firstElement instanceof DBViewRepNode) {
                DBViewRepNode node = (DBViewRepNode) firstElement;
                semanticDiscoveryAction = new SemanticDiscoveryAction(node.getTdView());
            } else if (firstElement instanceof DFTableRepNode) {
                DFTableRepNode node = (DFTableRepNode) firstElement;
                semanticDiscoveryAction = new SemanticDiscoveryAction(node.getMetadataTable());
            }
        }
        menu.add(semanticDiscoveryAction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#isShowMenu()
     */
    @Override
    public boolean isShowMenu() {
        if (CorePlugin.getDefault().getSemanticStudioService() == null) {
            return false;
        }
        return super.isShowMenu();
    }

}
