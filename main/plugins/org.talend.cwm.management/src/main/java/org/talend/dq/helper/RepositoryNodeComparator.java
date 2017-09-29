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

import java.util.Comparator;

import org.talend.repository.model.IRepositoryNode;

/**
 * comparator for IRepositoryNode.
 * 
 * DOC Administrator class global comment. Detailled comment
 */
public class RepositoryNodeComparator implements Comparator<IRepositoryNode> {

    public int compare(IRepositoryNode o1, IRepositoryNode o2) {
        if (o1 == null || o2 == null) {
            return 0;
        }
        String label1 = RepositoryNodeHelper.getDisplayLabel(o1);
        String label2 = RepositoryNodeHelper.getDisplayLabel(o2);
        if ("".equals(label1) || "".equals(label2)) { //$NON-NLS-1$ //$NON-NLS-2$
            return 0;
        }
        return label1.compareTo(label2);
    }

}
