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
package org.talend.cwm.compare.factory;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.compare.factory.comparisonlevel.CatalogSchemaComparisonLevel;
import org.talend.cwm.compare.factory.comparisonlevel.DataProviderComparisonLevel;
import org.talend.cwm.compare.factory.comparisonlevel.FileMetadataTableComparisonLevel;
import org.talend.cwm.compare.factory.comparisonlevel.RepositoryObjectComparisonLevel;
import org.talend.cwm.compare.factory.comparisonlevel.SelectedLocalComparison;
import org.talend.cwm.compare.factory.comparisonlevel.TableViewComparisonLevel;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;

/**
 * This factory use to create the <code>IComparisonLevel</code> object.
 */
public final class ComparisonLevelFactory {

    /**
     * DOC rli CompareLevelFactory constructor comment.
     */
    private ComparisonLevelFactory() {
    }

    public static IComparisonLevel creatComparisonLevel(Object selectedObject) {
        IComparisonLevel comparisonLevel = null;
        if (selectedObject instanceof DBConnectionRepNode) {
            // MOD klliu 2011-02-24 bug 19015
            IRepositoryViewObject resObject = ((DBConnectionRepNode) selectedObject).getObject();
            comparisonLevel = new RepositoryObjectComparisonLevel(resObject);
        } else if (selectedObject instanceof DBTableFolderRepNode) {
            // MOD mzhao FolderNode param need to pass for later reloading from this folder.
            DBTableFolderRepNode dbFolderNode = (DBTableFolderRepNode) selectedObject;
            comparisonLevel = new CatalogSchemaComparisonLevel(dbFolderNode);
        } else if (selectedObject instanceof DBViewFolderRepNode) {
            // TODO tested for bug :TDQ-1533, but can't enter this if anymore, so this if maybe can be deleted.
            // MOC yyin 20121101, TDQ-6092, add a condition for the db which donot have catalog, like oracle.
            if ((null == ((DBViewFolderRepNode) selectedObject).getCatalog())
                    && (((DBViewFolderRepNode) selectedObject).getParent() instanceof DBConnectionFolderRepNode)) {
                // MOD yyi 2011-07-14 21512:the selected obj is db connection in this case
                IRepositoryViewObject resObject = ((DBViewFolderRepNode) selectedObject).getParent().getObject();
                comparisonLevel = new RepositoryObjectComparisonLevel(resObject);
            } else {
                DBViewFolderRepNode dbFolderNode = (DBViewFolderRepNode) selectedObject;
                comparisonLevel = new CatalogSchemaComparisonLevel(dbFolderNode);
            }
        } else if (selectedObject instanceof DBColumnFolderRepNode) {
            DBColumnFolderRepNode dbFolderNode = (DBColumnFolderRepNode) selectedObject;
            comparisonLevel = new TableViewComparisonLevel(dbFolderNode);
        } else if (selectedObject instanceof MetadataTable) {
            MetadataTable resObject = (MetadataTable) selectedObject;
            comparisonLevel = new FileMetadataTableComparisonLevel(resObject);
        } else if (selectedObject instanceof Connection) {
            // MOD qiongli 2011-9-5 feature TDQ-3317.
            comparisonLevel = new DataProviderComparisonLevel(selectedObject);
        } else {
            comparisonLevel = null;
        }

        return comparisonLevel;
    }

    /**
     * 
     * DOC mzhao Compare the selected two elements.
     * 
     * @param selectedObject1 first selected element.
     * @param selectedObject2 second selected element.
     * @return IComparisonLevel
     */
    public static IComparisonLevel creatComparisonLevel(Object selectedObject1, Object selectedObject2) {
        IComparisonLevel comparisonLevel = new SelectedLocalComparison(selectedObject1, selectedObject2);
        return comparisonLevel;
    }
}
