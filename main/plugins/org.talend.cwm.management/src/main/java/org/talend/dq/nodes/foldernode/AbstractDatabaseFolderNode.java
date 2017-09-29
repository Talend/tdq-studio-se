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
package org.talend.dq.nodes.foldernode;


/**
 * DOC rli class global comment. Detailled comment
 */
public abstract class AbstractDatabaseFolderNode extends AbstractFolderNode {

    /**
     * DOC rli AbstractDatabaseFolderNode constructor comment.
     * 
     * @param name
     */
    public AbstractDatabaseFolderNode(String name) {
        super(name);
    }

    public void loadChildren() {
    }
}
