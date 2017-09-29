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
package org.talend.cwm.compare.ui.actions;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.factory.ComparisonLevelFactory;
import org.talend.cwm.compare.factory.IComparisonLevel;
import org.talend.cwm.compare.i18n.Messages;
import org.talend.cwm.compare.ui.ImageLib;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;

/**
 * DOC rli class global comment. Detailled comment
 */
public class PopComparisonUIAction extends Action {

    private static Logger log = Logger.getLogger(PopComparisonUIAction.class);

    private Object selectedObject;

    public void setSelectedObject(Object selectedObject) {
        this.selectedObject = selectedObject;
    }

    public PopComparisonUIAction(String menuText) {
        this(null, menuText);

    }

    public PopComparisonUIAction(Object selectedNode, String menuText) {
        super(menuText);
        this.selectedObject = selectedNode;
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.COMPARE_IMAGE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                final IComparisonLevel creatComparisonLevel = ComparisonLevelFactory.creatComparisonLevel(selectedObject);

                try {
                    if (creatComparisonLevel != null) {
                        creatComparisonLevel.popComparisonUI(new UiHandler());
                    } else {
                        String errMsg = Messages.getString("PopComparisonUIAction.dontSupport"); //$NON-NLS-1$
                        log.error(errMsg, new Exception(errMsg));
                    }
                } catch (ReloadCompareException e) {
                    MessageDialog.openError(shell,
                            Messages.getString("PopComparisonUIAction.ErrorDuringComparison"), e.getMessage() //$NON-NLS-1$
                            );
                    log.error(e, e);
                }
            }
        };
        try {
            ProgressUI.popProgressDialog(op, false, false);
        } catch (InvocationTargetException e) {
            MessageDialog
                    .openInformation(
                            shell,
                            Messages.getString("PopComparisonUIAction.connectionFailure"), Messages.getString("PopComparisonUIAction.checkConnectionFailure")); //$NON-NLS-1$ //$NON-NLS-2$
            log.error(e, e);
        } catch (InterruptedException e) {
            log.error(e, e);
        }

    }
}
