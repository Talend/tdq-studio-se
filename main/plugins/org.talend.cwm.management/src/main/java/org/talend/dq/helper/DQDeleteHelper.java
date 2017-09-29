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
package org.talend.dq.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public final class DQDeleteHelper {

    private DQDeleteHelper() {
    }

    /**
     * 
     * if these items in recycle bin are depended by others which is not in recycle bin,show a warning and return.
     * 
     * @param allNodes
     * @param isCurrentPerspectiveDQ
     * @return these list will be used to pop a dialog and display the detail nodes which are depended by others.
     */
    public static List<IRepositoryNode> getCanNotDeletedNodes(Collection<IRepositoryNode> allNodes, boolean isCurrentPerspectiveDQ) {
        List<IRepositoryNode> canNotDeletedNodes = new ArrayList<IRepositoryNode>();
        if (allNodes == null) {
            return canNotDeletedNodes;
        }

        for (IRepositoryNode node : allNodes) {
            List<ModelElement> dependencies = EObjectHelper.getDependencyClients(node);

            if (dependencies == null || dependencies.isEmpty()) {
                continue;
            }
            // if the current perspective is not DQ,no need to judge its client dependences are in recycle bin.
            if (!isCurrentPerspectiveDQ) {
                canNotDeletedNodes.add(node);
                continue;
            }
            for (ModelElement mod : dependencies) {
                Property property = PropertyHelper.getProperty(mod);
                if (property == null) {
                    continue;
                }
                Item item = property.getItem();
                if (item != null && !item.getState().isDeleted()) {
                    canNotDeletedNodes.add(node);
                }
            }
        }
        return canNotDeletedNodes;
    }

}
