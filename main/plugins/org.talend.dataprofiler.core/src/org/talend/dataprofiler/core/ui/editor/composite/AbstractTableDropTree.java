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
package org.talend.dataprofiler.core.ui.editor.composite;

import java.util.List;

import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * The interface class to handle the change when drop tables.
 */
public abstract class AbstractTableDropTree extends AbstractColumnDropTree {

    public static final String TABLEVIEWER_KEY = "TABLEVIEWER_KEY"; //$NON-NLS-1$

    public abstract void dropTables(List<NamedColumnSet> sets, int index);

    public abstract void dropWhereRules(Object data, List<RuleRepNode> nodes, int index);

    public abstract boolean canDrop(NamedColumnSet set);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#addElements(org.talend.dataprofiler.core
     * .model.ModelElementIndicator[])
     */
    @Override
    public void addElements(ModelElementIndicator[] elements) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#canDrop(org.talend.repository.model.
     * IRepositoryNode)
     */
    @Override
    public boolean canDrop(IRepositoryNode reposNode) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#setElements(org.talend.dataprofiler.core
     * .model.ModelElementIndicator[])
     */
    @Override
    protected void setElements(ModelElementIndicator[] modelElementIndicator) {
        // TODO Auto-generated method stub

    }
}
