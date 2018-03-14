// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
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

    /**
     * 
     * Find client dependencies by contextId. it might be Connection
     * 
     * @param contextId
     * @return
     */
    public static List<ModelElement> getContextClientDepend(String contextId) {
        List<ModelElement> modelElementLs = new ArrayList<ModelElement>();
        if (StringUtils.isEmpty(contextId)) {
            return modelElementLs;
        }
        for (ItemRecord record : ItemRecord.getAllItemRecords()) {
            Property property = record.getProperty();
            if (property == null) {
                continue;
            }
            Item item = property.getItem();
            if (item instanceof ConnectionItem) {
                Connection connection = ((ConnectionItem) item).getConnection();
                if (connection.isContextMode() && contextId.equals(connection.getContextId())) {
                    modelElementLs.add(record.getElement());
                }
            }
        }
        return modelElementLs;
    }
}
