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
package org.talend.dataprofiler.core.ui.wizard.analysis.provider;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.talend.dataprofiler.core.model.nodes.analysis.AnalysisTypeNode;
import org.talend.dq.nodes.analysis.AbstractAnalysisNode;

/**
 * @author huangssssx
 */
public class AnalysisTypeContentProvider implements ITreeContentProvider {

    public AnalysisTypeContentProvider() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) {
        AbstractAnalysisNode analysisNode = (AnalysisTypeNode) parentElement;

        Object[] childrenNode = analysisNode.getChildren();
        if (childrenNode == null || childrenNode.length == 0) {
            return new Object[0];
        } else {
            return childrenNode;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        return ((AnalysisTypeNode) element).getParent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        AbstractAnalysisNode analysisTypeNode = (AnalysisTypeNode) element;
        Object[] childrenNode = analysisTypeNode.getChildren();
        return !(childrenNode == null || childrenNode.length == 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof List) {
            return ((List) inputElement).toArray();
        } else {
            return new Object[0];
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
