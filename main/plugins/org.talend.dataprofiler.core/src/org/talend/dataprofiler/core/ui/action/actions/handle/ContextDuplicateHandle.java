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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;


/**
 * 
 * @author qiongli
 * Duplicate a context node on DQ side
 *
 */
public class ContextDuplicateHandle implements IDuplicateHandle {

    IRepositoryNode node;

    private static final Logger LOG = Logger.getLogger(ContextDuplicateHandle.class);

    /**
     * 
     * @param node selected node
     */
    public ContextDuplicateHandle(IRepositoryNode node) {
        this.node = node;
    }

    /**
     * copy a new item(same as DI)
     */
    public Item duplicateItem(Item oldItem, String newName) throws BusinessException {
        IPath path = RepositoryNodeUtilities.getPath(node);
        Item newContextItem = null;
        try {
            newContextItem = ProxyRepositoryFactory.getInstance().copy(oldItem, path, newName);
        } catch (PersistenceException e) {
            LOG.error(e);
        }

        return newContextItem;
    }

}
