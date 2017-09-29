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
public  interface  IAnalysisTypeNode {

    //get and set the name of node
    public String getName();
    public void setName(String name);
    
    //get and set the children
    public void setChildren(Object[] children);
    public Object[] getChildren();
    
    //get the parent
    public Object getParent();
    public void setParent(Object parent);
}
