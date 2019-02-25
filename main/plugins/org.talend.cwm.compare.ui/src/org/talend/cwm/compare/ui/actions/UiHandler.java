// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.cwm.compare.ui.actions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.i18n.internal.DefaultMessagesImpl;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.factory.IUIHandler;
import org.talend.cwm.compare.i18n.Messages;
import org.talend.cwm.compare.ui.editor.ModelElementCompareEditorLauncher;
import org.talend.dataprofiler.core.ui.dialog.message.DeleteModelElementConfirmDialog;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class UiHandler implements IUIHandler {


    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.cwm.compare.factory.IUIHandler#popComparisonUI(org.eclipse.core.runtime.IPath, java.lang.String,
     * java.lang.Object, boolean)
     */
    public void popComparisonUI(IPath diffResourcePath, String dbName, Object selectedObject, boolean compareEachOther)
            throws ReloadCompareException {
        new ModelElementCompareEditorLauncher(dbName, selectedObject, compareEachOther).open(diffResourcePath);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see
     * org.talend.cwm.compare.factory.IUIHandler#popRemoveElement(org.talend.core.model.metadata.builder.connection.
     * Connection)
     */
    public void popRemoveElement(final Connection provider) {
        final Display display = PlatformUI.getWorkbench().getDisplay();
        display.asyncExec(new Runnable() {
            public void run() {
                String titleMessage = DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.confirmElementDelete"); //$NON-NLS-1$
                DeleteModelElementConfirmDialog.showElementImpactDialog(Display.getDefault().getActiveShell(),
                        new ModelElement[] { provider }, titleMessage, Messages.getString("UiHandler.followingAnalysisBeImpact")); //$NON-NLS-1$
            }
        });
    }

}
