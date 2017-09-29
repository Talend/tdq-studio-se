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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.cellEditor;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.dataprofiler.common.ui.dialog.MatchCustomJarSelectDialog;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.helper.CustomAttributeMatcherHelper;
import org.talend.resource.ResourceManager;

/**
 * open fileDialog
 * 
 */
public class jarFileCellEditor extends DialogCellEditor {

    /**
     * 
     * 
     * @param parent
     */
    public jarFileCellEditor(Composite parent) {
        super(parent);
    }

    /**
     * 
     * 
     * @param parent
     * @param style
     */
    public jarFileCellEditor(Composite parent, int style) {
        super(parent, style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
     */
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        MatchCustomJarSelectDialog dialog = createCustomMatcherJarCheckedTreeSelectionDialog(cellEditorWindow.getShell(),
                ResourceManager.getUDIJarFolder());
        int returnCode = dialog.open();
        String path = null;
        if (Window.OK == returnCode) {
            path = dialog.getSelectResult();
        }
        return path;
    }

    /**
     * 
     * zshen Comment method "createUdiJarCheckedTreeSelectionDialog".
     * 
     * @param JarProject
     * @return
     */
    public MatchCustomJarSelectDialog createCustomMatcherJarCheckedTreeSelectionDialog(Shell parent, IFolder JarProject) {
        MatchCustomJarSelectDialog dialog = new MatchCustomJarSelectDialog(parent, new CustomMatcherLabelProvider(),
                new CustomMatcherJarContentProvider());
        dialog.setCheckValue((String) doGetValue());
        dialog.setInput(JarProject);
        dialog.setCheckedElements(CustomAttributeMatcherHelper.splitJarPath((String) doGetValue()));
        dialog.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof File) {
                    File file = (File) element;
                    if ("jar".equals(new Path(file.getName()).getFileExtension())) { //$NON-NLS-1$
                        return true;
                    }
                }
                return false;
            }
        });
        dialog.setContainerMode(true);
        dialog.setTitle(DefaultMessagesImpl.getString("jarFileCellEditor.matcherSelector")); //$NON-NLS-1$
        dialog.setMessage(DefaultMessagesImpl.getString("jarFileCellEditor.matchers")); //$NON-NLS-1$
        dialog.setSize(80, 30);
        return dialog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.DialogCellEditor#updateContents(java.lang.Object)
     */
    @Override
    protected void updateContents(Object value) {
        if (getDefaultLabel() == null) {
            return;
        }

        String text = StringUtils.EMPTY;
        if (value != null && value instanceof String) {
            text = CustomAttributeMatcherHelper.getClassName(value.toString());
        }
        getDefaultLabel().setText(text);
    }
}
