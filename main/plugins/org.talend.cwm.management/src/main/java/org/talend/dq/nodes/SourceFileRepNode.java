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

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataquality.properties.TDQSourceFileItem;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class SourceFileRepNode extends DQRepositoryNode {

    private TDQSourceFileItem sourceFileItem;

    public TDQSourceFileItem getSourceFileItem() {
        return this.sourceFileItem;
    }

    /**
     * DOC klliu SourceFileRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public SourceFileRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        if (object != null && object.getProperty() != null) {
            Item item = object.getProperty().getItem();
            if (item != null && item instanceof TDQSourceFileItem) {
                this.sourceFileItem = (TDQSourceFileItem) item;
            }
        }
    }

    // Added TDQ-7672 Add lock/unlock in context menu for sql source file
    @Override
    public boolean canExpandForDoubleClick() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getLabel() + " " + getObject().getVersion(); //$NON-NLS-1$
    }
}
