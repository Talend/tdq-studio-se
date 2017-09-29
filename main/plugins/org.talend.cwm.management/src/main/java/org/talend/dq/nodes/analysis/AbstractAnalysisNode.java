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
package org.talend.dq.nodes.analysis;



/**
 * @author zqin
 *
 */
public abstract class AbstractAnalysisNode implements IAnalysisTypeNode {
    
    protected String name;
    
    protected String literal;
    
    protected Object[] children;
    
    protected Object parent;
    
    public AbstractAnalysisNode(String name) {
        this.name = name;
    }
    
    public AbstractAnalysisNode(String name, String literal, Object parent) {
        this.name = name;
        this.parent = parent;
        this.literal = literal;
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.core.model.nodes.analysis.IAnalysisTypeNode#getChildren()
     */
    public Object[] getChildren() {
        return this.children;
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.core.model.nodes.analysis.IAnalysisTypeNode#getName()
     */
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.core.model.nodes.analysis.IAnalysisTypeNode#setChildren(java.util.List)
     */
    public void setChildren(Object[] children) {
        this.children = children;
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.core.model.nodes.analysis.IAnalysisTypeNode#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.core.model.nodes.analysis.IAnalysisTypeNode#getParent()
     */
    public Object getParent() {
        return this.parent;
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.core.model.nodes.analysis.IAnalysisTypeNode#setParent(java.lang.Object)
     */
    public void setParent(Object parent) {
        this.parent = parent;
    }

    
    /**
     * @return the literal
     */
    public String getLiteral() {
        return this.literal;
    }

    
    /**
     * @param literal the literal to set
     */
    public void setLiteral(String literal) {
        this.literal = literal;
    }


}
