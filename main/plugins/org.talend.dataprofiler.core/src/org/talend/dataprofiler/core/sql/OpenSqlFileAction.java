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

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.talend.commons.exception.BusinessException;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.exception.ExceptionFactory;
import org.talend.dataprofiler.core.exception.ExceptionHandler;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.perspective.ChangePerspectiveAction;
import org.talend.dq.helper.SqlExplorerUtils;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class OpenSqlFileAction extends Action {

    protected static Logger log = Logger.getLogger(OpenSqlFileAction.class);

    private List<IFile> folder;

    /**
     * DOC qzhang AddSqlFileAction constructor comment.
     * 
     * @param selectedFiles
     */
    public OpenSqlFileAction(List<IFile> selectedFiles) {
        setText(DefaultMessagesImpl.getString("OpenSqlFileAction.openIn", PluginConstant.DATAEXPLORER_PERSPECTIVE)); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.CREATE_SQL_ACTION));
        this.folder = selectedFiles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        try {
            for (IFile file : folder) {
                if (!file.exists()) {
                    BusinessException createBusinessException = ExceptionFactory.getInstance().createBusinessException(
                            file.getName());
                    throw createBusinessException;
                }
            }
            // ADD xqliu 2010-08-20 bug 13729
            new ChangePerspectiveAction(PluginConstant.SE_ID).run();
            // ~ 13729
            IWorkbenchWindow aww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            IWorkbenchPage ap = aww.getActivePage();
            for (IFile file : folder) {
                try {
                    IDE.openEditor(ap, file, SqlExplorerUtils.SQLEDITOR_ID, true);
                } catch (PartInitException e) {
                    log.error(e, e);
                }
            }
        } catch (BusinessException e) {
            ExceptionHandler.process(e, Level.FATAL);
        }
    }

}
