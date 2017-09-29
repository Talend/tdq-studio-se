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
package org.talend.dq.nodes.indicator.impl;

import org.talend.dataquality.PluginConstant;
import org.talend.dq.nodes.indicator.IIndicatorNode;

/**
 * The User Definition node
 * 
 */
public class UDINode extends IndicatorNode {

    /**
     * DOC talend NormalNode constructor comment.
     * 
     * @param label
     */
    public UDINode(String label) {
        super(label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.IIndicatorNode#getChildren()
     */
    @Override
    public IIndicatorNode[] getChildren() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.IIndicatorNode#isIndicatorEnumNode()
     */
    @Override
    public boolean isIndicatorEnumNode() {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.AbstractNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public String getLabel() {
        if (label == null) {
            return PluginConstant.EMPTY_STRING;
        } else {
            return label;
        }
    }
}
