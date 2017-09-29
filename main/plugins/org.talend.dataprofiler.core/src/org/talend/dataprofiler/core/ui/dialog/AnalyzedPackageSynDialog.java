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

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.dataquality.analysis.Analysis;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC mzhao analyzed columns synchronized dialog. 2009-06-02 Feature 5887
 */
public class AnalyzedPackageSynDialog extends AnalyzedElementSynDialog {

    private static Logger log = Logger.getLogger(AnalyzedColumnsSynDialog.class);

    private EList<ModelElement> analyzedElements = null;

    public AnalyzedPackageSynDialog(Shell parent, Analysis analysis, Connection dataprovider, EList<ModelElement> analyzedElements) {
        super(parent, analysis, dataprovider);
        this.analyzedElements = analyzedElements;
    }

    @Override
    public void reloadInputModel() {
        Package anaPackage = null;
        modelInput.clear();
        synedEleMap.clear();
        for (ModelElement element : analyzedElements) {
            try {
                anaPackage = (Package) element;
                synedEleMap.put(anaPackage, null);
                Package connPackage = null;
                for (Package pk : newDataProvider.getDataPackage()) {
                    if (pk.getName().equalsIgnoreCase(anaPackage.getName())) {
                        connPackage = pk;
                        break;
                    }
                }
                if (connPackage == null) {
                    SynTreeModel synTreeModel = new SynTreeModel(anaPackage);
                    synTreeModel.setOldDataProvElement(anaPackage);
                    // synTreeModel.setNewDataProvElement(connPackage);
                    modelInput.add(synTreeModel);
                    continue;
                }
                synedEleMap.put(anaPackage, connPackage);
            } catch (Exception e) {
                log.error(e, e);
                e.printStackTrace();
            }
        }
    }
}
