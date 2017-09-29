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
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.PackageHelper;
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
public class AnalyzedColumnSetsSynDialog extends AnalyzedElementSynDialog {

    private static Logger log = Logger.getLogger(AnalyzedColumnsSynDialog.class);

    private EList<ModelElement> analyzedElements = null;

    public AnalyzedColumnSetsSynDialog(Shell parent, Analysis analysis, Connection dataprovider,
            EList<ModelElement> analyzedElements) {
        super(parent, analysis, dataprovider);
        this.analyzedElements = analyzedElements;
    }

    @Override
    public void reloadInputModel() {
        ColumnSet anaColumnSet = null;
        modelInput.clear();
        synedEleMap.clear();
        for (ModelElement element : analyzedElements) {
            try {
                anaColumnSet = (ColumnSet) element;
                synedEleMap.put(anaColumnSet, null);

                Package anaPackage = ColumnSetHelper.getParentCatalogOrSchema(anaColumnSet);
                if (anaPackage == null) {
                    return;
                }
                Package connPackage = null;
                // MOD by zshen for bug 16775 on 2010.12.28 for mssql case
                for (Package pk : newDataProvider.getDataPackage()) {
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
                    SynTreeModel synTreeModel = new SynTreeModel(anaColumnSet);
                    synTreeModel.setOldDataProvElement(anaPackage);
                    // synTreeModel.setNewDataProvElement(connPackage);
                    modelInput.add(synTreeModel);
                    break;
                }

                List<? extends MetadataTable> connColumnSetList = null;
                // MOD by zshen for bug 16775 on 2010.12.28 for mssql case
                if (connPackage instanceof Catalog && anaPackage instanceof Schema) {
                    for (Schema sche : CatalogHelper.getSchemas((Catalog) connPackage)) {
                        if (sche.getName().equalsIgnoreCase(anaPackage.getName())) {
                            connPackage = sche;
                            // if (anaColumnSet instanceof TdTable) {
                            // connColumnSetList = PackageHelper.getTables(sche);
                            // } else {
                            // connColumnSetList = PackageHelper.getViews(sche);
                            // }
                            // break;
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
                    SynTreeModel synTreeModel = new SynTreeModel(anaColumnSet);
                    synTreeModel.setOldDataProvElement(anaColumnSet);
                    synTreeModel.setNewDataProvElement(connPackage);
                    modelInput.add(synTreeModel);
                    continue;
                }
                synedEleMap.put(anaColumnSet, connColumnSet);
            } catch (Exception e) {
                log.error(e, e);
                e.printStackTrace();
            }
        }
    }
}
