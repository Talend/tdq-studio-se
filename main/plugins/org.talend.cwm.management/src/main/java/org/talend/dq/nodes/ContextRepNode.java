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
package org.talend.dq.nodes;

import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.RepositoryNode;


/**
 * @author qiongli
 *
 */
public class ContextRepNode extends DQRepositoryNode {

    /**
     * @param object
     * @param parent
     * @param type
     * @param project
     */
    public ContextRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type, Project project) {
        super(object, parent, type, project);
        // TODO Auto-generated constructor stub
    }

    public ContextItem getContextItem() {
        Property property = this.getObject().getProperty();
        if (property != null && property.getItem() != null) {
            return ((ContextItem) property.getItem());
        }
        return null;
    }

}
