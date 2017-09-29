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
package org.talend.dataprofiler.core.ui.utils;

import java.io.File;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dq.helper.EObjectHelper;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by qiongli on Sep 26, 2012. some communal functions for import and export
 * 
 */
public class ImportAndExportUtils {

    /**
     * 
     * when uncheck an item,should uncheck related client depenedences.
     * 
     * @param dependencyClients
     */
    public static void iterateUncheckClientDependency(List<ModelElement> dependencyClients, CheckboxTreeViewer repositoryTree) {
        for (ModelElement dependency : dependencyClients) {
            List<ModelElement> iterdependencyClients = EObjectHelper.getDependencyClients(dependency);
            if (!iterdependencyClients.isEmpty()) {
                iterateUncheckClientDependency(iterdependencyClients, repositoryTree);
            }
            URI uri = EObjectHelper.getURI(dependency);
            if (uri != null) {
                String uriString = WorkspaceUtils.toFile(uri);
                File depFile = new File(uriString);
                ItemRecord findRecord = ItemRecord.findRecord(depFile);
                if (findRecord != null) {
                    repositoryTree.setChecked(findRecord, false);
                }
            }

        }
    }
}
