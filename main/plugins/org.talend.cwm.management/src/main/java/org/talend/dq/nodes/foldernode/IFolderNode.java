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
public interface IFolderNode {

    public static final int FILE_TYPE = -1;

    public static final int MODELELEMENT_TYPE = -2;

    public static final int MODELFOLDER_NODE_TYPE = 0;

    public static final int TABLEFOLDER_NODE_TYPE = 1;

    public static final int VIEWFOLDER_NODE_TYPE = 2;

    public static final int COLUMNFOLDER_NODE_TYPE = 3;

    public int getChildrenType();

    public String getName();

    /**
     * @return the children
     */
    public Object[] getChildren();

    /**
     * @param children the children to set
     */
    public void setChildren(Object[] children);

    public void setParent(Object parent);

    public Object getParent();

    public abstract void loadChildren();

    public int getFolderNodeType();

}
