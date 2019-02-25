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
package org.talend.dataprofiler.core.pattern.actions;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.resource.ResourceManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
 * 
 */
public class DeletePatternAction extends Action {

    protected static Logger log = Logger.getLogger(DeletePatternAction.class);

    private List<IFile> selectedFiles;

    /**
     * DOC qzhang AddSqlFileAction constructor comment.
     * 
     * @param folder
     */
    public DeletePatternAction(List<IFile> selectedFiles) {
        setText(DefaultMessagesImpl.getString("DeletePatternAction.deleteRegularPattern")); //$NON-NLS-1$
        // setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.
        // CREATE_SQL_ACTION));
        this.selectedFiles = selectedFiles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IFolder sourceFiles = ResourceManager.getPatternFolder();
        for (IFile file : selectedFiles) {
            if (MessageDialog.openConfirm(DisplayUtils.getDefaultShell(false),
                    DefaultMessagesImpl.getString("DeletePatternAction.deleteRegularPatternFile"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("DeletePatternAction.areYouDeleteRegularPatternFile", file.getName()))) { //$NON-NLS-1$
                try {
                    if (file.exists()) {
                        file.delete(true, null);
                    }
                } catch (CoreException e) {
                    log.error(e, e);
                }
            }
        }
        try {
            ProxyRepositoryManager.getInstance().save();
            sourceFiles.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (CoreException e) {
            log.error(e, e);
        }
    }

}
