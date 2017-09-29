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

package org.talend.cwm.compare.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.foldernode.IFolderNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC mzhao class global comment. Detailled comment
 */
public class SubelementCompareAction extends Action {

    public static final int COLUMN_COMPARE = 0;

    public static final int TABLE_COMPARE = 1;

    public static final int VIEW_COMPARE = 2;

    private String text = null;

    // private ModelContentMergeDiffTab diffTabLeft = null;

    private int actionType;

    private Object selectedOjbect = null;

    private PopComparisonUIAction popCompUIAction = null;

    // public SubelementCompareAction(String showText, ModelContentMergeDiffTab diffTabLeft, Object selectedOjbect, int
    // actionType) {
    // text = showText;
    // this.diffTabLeft = diffTabLeft;
    // this.selectedOjbect = selectedOjbect;
    // this.actionType = actionType;
    // popCompUIAction = new PopComparisonUIAction(text);
    // }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void run() {
        // IStructuredSelection selection = (IStructuredSelection) diffTabLeft.getSelection();
        // EObject selectedElement = null;
        // if (selection.toList().size() == 1) {
        // selectedElement = (EObject) selection.getFirstElement();
        // IFolderNode folderNode = null;
        // if (selectedElement instanceof ColumnSet) {
        // IRepositoryViewObject reposViewObj = ((RepositoryNode) selectedOjbect).getParent().getObject();
        // Package parentPackage = null;
        // if (reposViewObj instanceof ISubRepositoryObject) {
        // parentPackage = (Package) ((ISubRepositoryObject) reposViewObj).getModelElement();
        // }
        //
        // // MOD mzhao 2009-08-05 Bug 8581
        // // PackageHelper.addColumnSet((ColumnSet) selectedElement,
        // // parentPackage);
        // // ((ColumnSet) selectedElement).setNamespace(parentPackage);
        // ModelElement matchedElement = findMatchedModelElement(parentPackage, selectedElement);
        // // FIXME folderNode is never used.
        // folderNode = FolderNodeHelper.getFolderNodes(matchedElement)[0];
        // IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(matchedElement);
        //
        // openComparisonEditor(repositoryNode.getChildren().get(0));
        // } else if (selectedElement instanceof Catalog) {
        // // Judge and see if there are schemas under the catalog.(Case of
        // // SQL Servers)
        // Catalog catalog = (Catalog) selectedElement;
        // List<Schema> schemas = CatalogHelper.getSchemas(catalog);
        // if (schemas != null && schemas.size() > 0) {
        // IRepositoryViewObject reposViewObj = (IRepositoryViewObject) selectedOjbect;
        // ConnectionItem item = (ConnectionItem) reposViewObj.getProperty().getItem();
        // Connection conn = item.getConnection();
        // if (catalog.getDataManager().isEmpty()) {
        // catalog.getDataManager().add(conn);
        // }
        // popCompUIAction.setSelectedObject(getTableOrViewFolder(getFirstSchema(catalog)));
        // popCompUIAction.run();
        // } else {
        // IRepositoryNode repositoryfolderNode = getTableOrViewFolder(selectedElement);
        // openComparisonEditor(repositoryfolderNode);
        // }
        // } else if (selectedElement instanceof Schema) {
        // IRepositoryNode repositoryfolderNode = getTableOrViewFolder(selectedElement);
        // openComparisonEditor(repositoryfolderNode);
        // } else {
        // return;
        // }
        //
        // // Remove namespace link
        // if (selectedElement instanceof ColumnSet) {
        // IRepositoryViewObject reposViewObj = ((RepositoryNode) selectedOjbect).getParent().getObject();
        // Package parentPackage = null;
        // if (reposViewObj instanceof ISubRepositoryObject) {
        // parentPackage = (Package) ((ISubRepositoryObject) reposViewObj).getModelElement();
        // }
        // // FIXME the originPackage might be Null, and we should never used a new object to replace the old one.
        // Package originPackage = parentPackage;
        // if (originPackage.eIsProxy()) {
        // originPackage = (Package) EObjectHelper.resolveObject(originPackage);
        // }
        // PackageHelper.removeColumnSet((ColumnSet) selectedElement, originPackage);
        // EMFSharedResources.getInstance().saveResource(originPackage.eResource());
        // }
        // }
    }

    /**
     * get the first Schema under the Catalog.
     * 
     * @param catalog
     * @return
     */
    private Schema getFirstSchema(Catalog catalog) {
        Schema schema = null;
        List<Schema> schemas = CatalogHelper.getSchemas(catalog);
        if (schemas != null && schemas.size() > 0) {
            for (Schema tdSchema : schemas) {
                schema = tdSchema;
                break;
            }
        }
        return schema;
    }

    /**
     * DOC klliu Comment method "openComparisonEditor".
     * 
     * @param folderNode
     */
    public void openComparisonEditor(IFolderNode folderNode) {
        folderNode.loadChildren();
        popCompUIAction.setSelectedObject(folderNode);
        popCompUIAction.run();
    }

    /**
     * DOC zshen Comment method "openComparisonEditor".
     * 
     * @param repositoryfolderNode
     */
    public void openComparisonEditor(IRepositoryNode repositoryfolderNode) {
        repositoryfolderNode.getChildren();
        popCompUIAction.setSelectedObject(repositoryfolderNode);
        popCompUIAction.run();
    }

    private IRepositoryNode getTableOrViewFolder(EObject selectedElement) {
        IRepositoryNode folderNode = null;
        ModelElement matchedElement = null;

        Connection conn = null;
        if (selectedOjbect instanceof Catalog) {
            conn = ConnectionHelper.getTdDataProvider(((Catalog) selectedOjbect));
        } else {
            IRepositoryViewObject reposViewObj = (IRepositoryViewObject) selectedOjbect;
            ConnectionItem item = (ConnectionItem) reposViewObj.getProperty().getItem();
            conn = item.getConnection();
        }
        matchedElement = findMatchedModelElement(conn, selectedElement);

        if (matchedElement != null) {
            IRepositoryNode repositoryNode = RepositoryNodeHelper.recursiveFind(matchedElement);
            if (repositoryNode != null) {
                if (actionType == TABLE_COMPARE) {
                    folderNode = repositoryNode.getChildren().get(0);
                } else if (actionType == VIEW_COMPARE) {
                    folderNode = repositoryNode.getChildren().get(1);
                }
            }
        }
        return folderNode;
    }

    /**
     * DOC klliu Comment method "findMatchedModelElement". don't understand this method!!!
     * 
     * @param parent
     * @param similarElement
     * @return
     */
    private ModelElement findMatchedModelElement(EObject parent, EObject similarElement) {
        if (parent instanceof Connection) {
            if (similarElement instanceof Schema) {
                Schema schema = (Schema) similarElement;

                // Case of MS SQL Server.
                if (selectedOjbect instanceof Catalog) { // ??????
                    List<Schema> schemas = CatalogHelper.getSchemas((Catalog) selectedOjbect);
                    if (schemas != null && schemas.size() > 0) {
                        for (Schema tdSchema : schemas) {
                            if (tdSchema.getName().equalsIgnoreCase((schema).getName())) {
                                return tdSchema;
                            }
                        }
                    }
                }

                // case of MS SQL Server, Postgresql......
                EObject eContainer = schema.eContainer();
                if (eContainer != null && eContainer instanceof Catalog) {
                    List<Catalog> catalogs = ConnectionHelper.getCatalogs((Connection) parent);
                    for (Catalog catalog : catalogs) {
                        if (((Catalog) eContainer).getName().equals(catalog.getName())) {
                            List<Schema> schemas = CatalogHelper.getSchemas(catalog);
                            if (schemas != null && schemas.size() > 0) {
                                for (Schema tdSchema : schemas) {
                                    if (tdSchema.getName().equalsIgnoreCase((schema).getName())) {
                                        return tdSchema;
                                    }
                                }
                            }
                        }
                    }
                }

                // Case of Oracle
                List<Schema> schames = ConnectionHelper.getSchema((Connection) parent);
                for (Schema schame : schames) {
                    if (schame.getName().equalsIgnoreCase((schema).getName())) {
                        return schame;
                    }
                }
            }

            // Case of Mysql
            if (similarElement instanceof Catalog) {
                List<Catalog> catalogs = ConnectionHelper.getCatalogs((Connection) parent);
                for (Catalog catalog : catalogs) {
                    if (catalog.getName().equalsIgnoreCase(((Catalog) similarElement).getName())) {
                        return catalog;
                    }
                }
            }

        } else if (parent instanceof Package) {
            // MOD klliu bug 15822 resolve proxy caused two Instance,so we must use one Instance
            if (parent != null && parent.eIsProxy()) {
                parent = EObjectHelper.resolveObject(parent);
            }
            // MOD mzhao bug 8581 2009-08-05
            List<ColumnSet> columnSets = new ArrayList<ColumnSet>();
            columnSets.addAll(PackageHelper.getTables((Package) parent));
            columnSets.addAll(PackageHelper.getViews((Package) parent));
            for (ModelElement columnSet : columnSets) {
                if (((ColumnSet) columnSet).getName().equalsIgnoreCase(((ColumnSet) similarElement).getName())) {
                    return columnSet;
                }
            }
        }
        return null;
    }
}
