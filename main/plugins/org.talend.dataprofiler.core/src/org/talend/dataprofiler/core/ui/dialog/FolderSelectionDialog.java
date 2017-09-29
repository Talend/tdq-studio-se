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
package org.talend.dataprofiler.core.ui.dialog;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceComparator;

/**
 */
public class FolderSelectionDialog extends ElementTreeSelectionDialog implements ISelectionChangedListener {

    /**
     * DOC bZhou FolderSelectionDialog constructor comment.
     * 
     * @param parent
     * @param labelProvider
     * @param contentProvider
     */
    public FolderSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
        setComparator(new ResourceComparator(ResourceComparator.NAME));
    }

    /**
     * DOC bZhou FolderSelectionDialog constructor comment.
     * 
     * @param parent
     */
    public FolderSelectionDialog(Shell parent) {
        this(parent, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent) {
        Composite result = (Composite) super.createDialogArea(parent);

        getTreeViewer().addSelectionChangedListener(this);

        applyDialogFont(result);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent
     * )
     */
    public void selectionChanged(SelectionChangedEvent event) {
    }

}
