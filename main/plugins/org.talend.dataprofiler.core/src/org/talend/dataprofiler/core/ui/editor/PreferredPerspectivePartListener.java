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
package org.talend.dataprofiler.core.ui.editor;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.part.EditorPart;

/**
 * PreferredPerspectivePartListener is to be registered using the extension point "org.eclipse.ui.startup". It will
 * register itself as listener for the activation of parts. When a part which implements IPrefersPerspective is
 * activated it will activate the preferred perspective of this part.
 */
public class PreferredPerspectivePartListener implements IPartListener, IStartup {

    private static Logger log = Logger.getLogger(PreferredPerspectivePartListener.class);

    public void earlyStartup() {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .addPartListener(new PreferredPerspectivePartListener());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public static void refresh(final IWorkbenchPart part) {
        if (!(part instanceof IPrefersPerspective)) {
            return;
        }

        final IWorkbenchWindow workbenchWindow = part.getSite().getPage().getWorkbenchWindow();

        IPerspectiveDescriptor activePerspective = workbenchWindow.getActivePage().getPerspective();
        final List<String> preferredPerspectiveId = ((IPrefersPerspective) part).getPreferredPerspectiveId();

        if (preferredPerspectiveId == null || preferredPerspectiveId.size() < 1) {
            return;
        }

        if (activePerspective == null || !preferredPerspectiveId.contains(activePerspective.getId())) {
            // Switching of the perspective is delayed using Display.asyncExec because switching the perspective while
            // the workbench is activating parts might cause conflicts.
            Display.getCurrent().asyncExec(new Runnable() {

                public void run() {
                    String perspectiveId = preferredPerspectiveId.get(0);
                    log.debug("Switching to preferred perspective " + perspectiveId + " for " + part.getClass()); //$NON-NLS-1$ //$NON-NLS-2$
                    try {
                        workbenchWindow.getWorkbench().showPerspective(perspectiveId, workbenchWindow);
                    } catch (WorkbenchException e) {
                        log.warn("Could not switch to preferred perspective " + perspectiveId + " for " + part.getClass(), e); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            });
        }
    }

    public void partActivated(IWorkbenchPart part) {

    }

    public void partBroughtToTop(IWorkbenchPart part) {
        if (part instanceof EditorPart) {
            refresh(part);
        }
    }

    public void partClosed(IWorkbenchPart part) {

    }

    public void partDeactivated(IWorkbenchPart part) {

    }

    public void partOpened(IWorkbenchPart part) {

    }
}
