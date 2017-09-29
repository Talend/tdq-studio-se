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

package org.talend.dataprofiler.core.sql;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.NewFolderDialog;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.helper.ProxyRepositoryManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
 * 
 */
public class CreateSourceFolderAction extends Action {

    private IFolder obj;

    /**
     * DOC qzhang CreateSourceFolderAction constructor comment.
     * 
     * @param obj
     */

    public CreateSourceFolderAction(IFolder obj) {
        this.obj = obj;
        setText(DefaultMessagesImpl.getString("CreateSourceFolderAction.newFolder")); //$NON-NLS-1$
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        NewFolderDialog dialog = new NewFolderDialog(Display.getDefault().getActiveShell(), obj);
        if (dialog.open() == NewFolderDialog.OK) {
            // Object[] res = dialog.getResult();
            ProxyRepositoryManager.getInstance().save();

            // IFolder resl = (IFolder) res[0];
            // IFolder folder = obj.getFolder(resl);
            // try {
            // resl.create(true, false, null);
            // } catch (CoreException e) {
            // }
        }
    }
}
