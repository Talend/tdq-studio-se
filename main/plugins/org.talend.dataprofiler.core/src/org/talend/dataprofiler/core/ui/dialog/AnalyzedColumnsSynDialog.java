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
package org.talend.dataprofiler.core.ui.dialog;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC mzhao analyzed columns synchronized dialog. 2009-06-02 Feature 5887
 */
public class AnalyzedColumnsSynDialog extends AnalyzedElementSynDialog {

    private static Logger log = Logger.getLogger(AnalyzedColumnsSynDialog.class);

    private EList<ModelElement> analyzedElements;

    public AnalyzedColumnsSynDialog(Shell parent, Analysis analysis, Connection dataprovider, EList<ModelElement> analyzedElements) {
        super(parent, analysis, dataprovider);
        this.analyzedElements = analyzedElements;
    }

    @Override
    public void reloadInputModel() {
        TdColumn column = null;
        modelInput.clear();
        synedEleMap.clear();
        for (ModelElement element : analyzedElements) {
            try {
                column = (TdColumn) element;
                synedEleMap.put(column, null);

                ColumnSet anaColumnSet = ColumnHelper.getColumnOwnerAsColumnSet(column);
                Package anaPackage = ColumnSetHelper.getParentCatalogOrSchema(anaColumnSet);
                if (anaPackage == null) {
                    return;
                }
                Package connPackage = null;

                for (Package pk : newDataProvider.getDataPackage()) {
                    // MOD mzhao bug 8567. 2009-08-10 (Case of MS SQL Server)
                    if (pk instanceof Catalog && anaPackage instanceof Schema) {
                        Catalog catl = CatalogHelper.getParentCatalog(anaPackage);
                        if (null != catl && pk.getName().equalsIgnoreCase(catl.getName())) {
                            connPackage = pk;
                            break;
                        }
                    } else if (null != anaPackage && pk.getName().equalsIgnoreCase(anaPackage.getName())) {
                        connPackage = pk;
                        break;
                    }
                }
                if (connPackage == null) {
                    SynTreeModel synTreeModel = new SynTreeModel(column);
                    synTreeModel.setOldDataProvElement(anaPackage);
                    // synTreeModel.setNewDataProvElement(connPackage);
                    modelInput.add(synTreeModel);
                    continue;
                }

                List<? extends MetadataTable> connColumnSetList = null;
                // MOD mzhao bug 8567. 2009-08-10 (Case of MS SQL Server)
                if (connPackage instanceof Catalog && anaPackage instanceof Schema) {
                    for (Schema sche : CatalogHelper.getSchemas((Catalog) connPackage)) {
                        if (sche.getName().equalsIgnoreCase(anaPackage.getName())) {
                            // MOD mzhao bug 8567. 2010-03-29.
                            connPackage = sche;
                            // if (anaColumnSet instanceof TdTable) {
                            // connColumnSetList = PackageHelper.getTables(sche);
                            // } else {
                            // connColumnSetList = PackageHelper.getViews(sche);
                            // }
                            break;
                        }
                    }
                }
                boolean loadFromDb = connPackage.getOwnedElement().size() == 0;
                if (anaColumnSet instanceof TdTable) {
                    connColumnSetList = DqRepositoryViewService.getTables(newDataProvider, connPackage, null, loadFromDb, false);
                    // connColumnSetList = PackageHelper.getTables(connPackage);
                    if (loadFromDb) {
                        for (MetadataTable table : connColumnSetList) {
                            PackageHelper.addMetadataTable(table, connPackage);
                        }
                    }
                    // connColumnSetList = PackageHelper.getTables(connPackage);
                } else {
                    connColumnSetList = DqRepositoryViewService.getViews(newDataProvider, connPackage, null, loadFromDb, false);
                    // connColumnSetList = PackageHelper.getViews(connPackage);
                    if (loadFromDb) {
                        for (MetadataTable table : connColumnSetList) {
                            PackageHelper.addMetadataTable(table, connPackage);
                        }
                    }
                }

                ColumnSet connColumnSet = null;
                for (Object colSet : connColumnSetList) {
                    if (((ColumnSet) colSet).getName().equalsIgnoreCase(anaColumnSet.getName())) {
                        connColumnSet = (ColumnSet) colSet;
                        break;
                    }
                }

                if (connColumnSet == null) {
                    SynTreeModel synTreeModel = new SynTreeModel(column);
                    synTreeModel.setOldDataProvElement(anaColumnSet);
                    synTreeModel.setNewDataProvElement(connPackage);
                    modelInput.add(synTreeModel);
                    continue;
                }

                TdColumn connColumn = null;

                // MOD xqliu 2012-05-04 TDQ-4853 should call getFeature(instead of getOwnedElement) method to get the
                // column list
                loadFromDb = connColumnSet.getFeature().size() == 0;
                // ~ TDQ-4853
                List<TdColumn> columns = DqRepositoryViewService.getColumns(newDataProvider, connColumnSet, null, loadFromDb);
                ColumnSetHelper.addColumns(connColumnSet, columns);
                for (TdColumn loopColumn : columns) {
                    if (loopColumn.getName().equalsIgnoreCase(column.getName())) {
                        connColumn = loopColumn;
                        break;
                    }
                }
                if (connColumn == null) {
                    SynTreeModel synTreeModel = new SynTreeModel(column);
                    synTreeModel.setOldDataProvElement(column);
                    synTreeModel.setNewDataProvElement(connColumnSet);
                    modelInput.add(synTreeModel);
                    continue;
                }
                synedEleMap.put(column, connColumn);
                if (!connColumn.getSqlDataType().getName().equals(column.getSqlDataType().getName())) {
                    SynTreeModel synTreeModel = new SynTreeModel(column);
                    synTreeModel.setOldDataProvElement(column.getSqlDataType());
                    // synTreeModel.setNewDataProvElement(connColumn
                    // .getSqlDataType());
                    modelInput.add(synTreeModel);
                    continue;
                }
            } catch (Exception e) {
                log.error(e, e);
                e.printStackTrace();
            }
        }
    }
}
