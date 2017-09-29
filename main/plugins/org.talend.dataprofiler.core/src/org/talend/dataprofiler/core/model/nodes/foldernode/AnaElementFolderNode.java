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
package org.talend.dataprofiler.core.model.nodes.foldernode;

import org.eclipse.emf.common.util.EList;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.nodes.foldernode.AbstractFolderNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class AnaElementFolderNode extends AbstractFolderNode {

    /**
     * DOC qzhang AnaElementFolderNode constructor comment.
     * 
     * @param analysedElements
     */
    public AnaElementFolderNode(EList<ModelElement> analysedElements) {
        super(DefaultMessagesImpl.getString("AnaElementFolderNode.analyzeElements")); //$NON-NLS-1$
        setChildren(analysedElements.toArray(new Object[0]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.model.nodes.foldernode.AbstractFolderNode#loadChildren()
     */
    @Override
    public void loadChildren() {
        // do nothing here ???
    }
}
