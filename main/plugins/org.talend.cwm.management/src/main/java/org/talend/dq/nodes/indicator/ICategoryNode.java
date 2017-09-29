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
package org.talend.dq.nodes.indicator;

/**
 * created by talend on Dec 30, 2014 Detailled comment
 * 
 */
public interface ICategoryNode extends IIndicatorNode {

    /**
     * 
     * Add child for current node
     * 
     * @param node the child node
     */
    public void addChildren(IIndicatorNode node);

    /**
     * 
     * Set children array to current node
     * 
     * @param children the array of children
     */
    public void setChildren(IIndicatorNode[] children);

    /**
     * Get the children of current node
     * 
     * @return The array of children
     */
    public IIndicatorNode[] getChildren();
}
