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

import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataquality.properties.TDQJrxmlItem;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class JrxmlTempleteRepNode extends DQRepositoryNode {

    /**
     * JrxmlTempleteRepNode constructor.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     * 
     */
    public JrxmlTempleteRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    public TDQJrxmlItem getJrxmlItem() {
        Property property = this.getObject().getProperty();
        if (property != null && property.getItem() != null) {
            return (TDQJrxmlItem) property.getItem();
        }
        return null;
    }

    @Override
    public String getLabel() {
        if (this.getJrxmlItem() != null) {
            return this.getJrxmlItem().getName();
        }
        return super.getLabel();
    }

}
