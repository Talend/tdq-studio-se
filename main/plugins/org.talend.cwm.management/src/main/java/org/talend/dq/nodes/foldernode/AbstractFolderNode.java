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
 * @author rli
 * 
 */
public abstract class AbstractFolderNode implements IFolderNode {

    private String name;

    private Object[] children;

    private Object parent;

    public AbstractFolderNode(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the children
     */
    public Object[] getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(Object[] children) {
        this.children = children;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public Object getParent() {
        return parent;
    }

    public abstract void loadChildren();

    public int getChildrenType() {
        return MODELELEMENT_TYPE;
    }

    public int getFolderNodeType() {
        return MODELFOLDER_NODE_TYPE;
    }

}
