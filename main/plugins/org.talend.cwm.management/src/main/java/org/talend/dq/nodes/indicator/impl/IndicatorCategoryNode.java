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

import org.apache.commons.lang.StringUtils;
import org.talend.dq.nodes.indicator.AbstractNode;
import org.talend.dq.nodes.indicator.ICategoryNode;
import org.talend.dq.nodes.indicator.IIndicatorNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * @author rli
 * 
 */
public class IndicatorCategoryNode extends AbstractNode implements ICategoryNode {

    public IndicatorCategoryNode(String label) {
        super(label);
    }

    public IndicatorCategoryNode(IndicatorEnum indicatorEnum) {
        super(indicatorEnum);
        createChildren(indicatorEnum);
    }

    public IndicatorCategoryNode(String label, IndicatorEnum indicatorEnum) {
        super(indicatorEnum);
        this.label = label;
        createChildren(indicatorEnum);
    }

    public IndicatorCategoryNode(String label, IndicatorEnum[] indicatorEnums) {
        super(label);
        this.creatChildren(indicatorEnums);
    }

    private void createChildren(IndicatorEnum childIndicatorEnum) {
        this.creatChildren(childIndicatorEnum.getChildren());

    }

    private void creatChildren(IndicatorEnum[] indicatorEnums) {
        IIndicatorNode[] childrenNodes = new IIndicatorNode[indicatorEnums.length];
        for (int i = 0; i < indicatorEnums.length; i++) {
            if (indicatorEnums[i].hasChildren()) {
                childrenNodes[i] = new IndicatorCategoryNode(indicatorEnums[i]);
                // ((IndicatorCategoryNode) childrenNodes[i]).createChildren(indicatorEnums[i]);
            } else {

                childrenNodes[i] = new IndicatorNode(indicatorEnums[i]);
            }
            childrenNodes[i].setParent(this);
        }
        this.setChildren(childrenNodes);
    }

    /**
     * the label should be a Internationalized string
     */
    @Override
    public String getLabel() {
        String displayLabel = label;
        if (StringUtils.isEmpty(displayLabel) && indicatorEnum != null) {
            displayLabel = this.indicatorEnum.getLabel();
        }
        return displayLabel;
    }

    public boolean isIndicatorEnumNode() {
        return indicatorEnum == null;
    }

    public void addChildren(IIndicatorNode node) {
        if (this.children != null) {
            IIndicatorNode[] nodes = new IIndicatorNode[this.children.length + 1];
            System.arraycopy(children, 0, nodes, 0, this.children.length);
            nodes[nodes.length - 1] = node;
            this.children = nodes;
        } else {
            this.children = new IIndicatorNode[] { node };
        }
    }

    /**
     * @param children the children to set
     */
    public void setChildren(IIndicatorNode[] children) {
        this.children = children;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.model.nodes.indicator.IIndicatorNode#getChildren()
     */
    public IIndicatorNode[] getChildren() {
        return children;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.AbstractNode#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.indicator.IIndicatorNode#getImageName()
     */
    public String getImageName() {
        return "folder.png"; //$NON-NLS-1$
    }

}
