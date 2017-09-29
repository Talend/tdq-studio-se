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
package org.talend.dataprofiler.core.ui.perspective;

import static org.talend.dataprofiler.core.PluginConstant.*;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;

/**
 * Changes the active perspective. <br/>
 * 
 * $Id: ChangePerspectiveAction.java 1774 2007-02-03 02:05:47 +0000 (Sat, 03 Feb 2007) bqian $
 * 
 */
public class ChangePerspectiveAction extends Action {

    private static Logger log = Logger.getLogger(ChangePerspectiveAction.class);

    private static final String SWITCH_TO_DATA_PROFILING = "Switch to " + PluginConstant.DATAPROFILER_PERSPECTIVE; //$NON-NLS-1$

    private static final String SWITCH_TO_DATA_DISCOVERY = "Switch to " + PluginConstant.DATAEXPLORER_PERSPECTIVE; //$NON-NLS-1$

    static ChangePerspectiveAction action;

    IPerspectiveRegistry registry = PlatformUI.getWorkbench().getPerspectiveRegistry();

    /** Id of the perspective to move to front. */
    private String perspectiveId;

    private boolean toolbar;

    public boolean isToolbar() {
        return this.toolbar;
    }

    public void setToolbar(boolean toolbar) {
        this.toolbar = toolbar;
    }

    /**
     * Constructs a new ChangePerspectiveAction.
     */
    public ChangePerspectiveAction(String perspectiveId) {
        super(perspectiveId, AS_CHECK_BOX);
        this.perspectiveId = perspectiveId;
        // MOD mzhao bug 8896
        if (action == null) {
            action = this;
        }
    }

    /**
     * DOC qzhang ChangePerspectiveAction constructor comment.
     */
    public ChangePerspectiveAction(boolean toolbar) {
        super(PluginConstant.SPACE_STRING); //$NON-NLS-1$
        this.toolbar = toolbar;
        action = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();

        if (!perspectiveId.equals(page.getPerspective().getId())) {
            try {
                workbench.showPerspective(perspectiveId, workbench.getActiveWorkbenchWindow());
            } catch (WorkbenchException e) {
                IStatus status = new Status(IStatus.ERROR, CorePlugin.PLUGIN_ID, IStatus.OK, "Show perspective failed.", e); //$NON-NLS-1$
                CorePlugin.getDefault().getLog().log(status);
            }
        }

        IPreferenceStore preferenceStore = CorePlugin.getDefault().getPreferenceStore();
        IViewPart findView = page.findView(CHEAT_SHEET_VIEW);
        if (PERSPECTIVE_ID.equals(perspectiveId)) {
            if (preferenceStore.getBoolean(CHEAT_SHEET_VIEW)) {
                try {
                    page.showView(CHEAT_SHEET_VIEW);
                } catch (PartInitException e) {
                    log.error(e, e);
                }
            }
            action.perspectiveId = SE_ID;
            action.setToolTipText(SWITCH_TO_DATA_DISCOVERY);
            IPerspectiveDescriptor fp = registry.findPerspectiveWithId(SE_ID);
            if (fp != null) {
                action.setImageDescriptor(fp.getImageDescriptor());
            }
        } else {
            preferenceStore.setValue(CHEAT_SHEET_VIEW, findView != null);
            if (findView != null) {
                page.hideView(findView);
            }
            action.perspectiveId = PERSPECTIVE_ID;
            action.setToolTipText(SWITCH_TO_DATA_PROFILING);
            IPerspectiveDescriptor fp = registry.findPerspectiveWithId(PERSPECTIVE_ID);
            if (fp != null) {
                action.setImageDescriptor(fp.getImageDescriptor());
            }
        }
    }

    /**
     * Getter for action.
     * 
     * @return the action
     */
    public static ChangePerspectiveAction getAction() {
        return action;
    }

    /**
     * DOC qzhang Comment method "switchTitle".
     */
    public void switchTitle() {
        String id2 = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId();
        String toID = SE_ID;
        if (SE_ID.equals(id2)) {
            toID = PERSPECTIVE_ID;
            setToolTipText(SWITCH_TO_DATA_PROFILING);
        } else {
            setToolTipText(SWITCH_TO_DATA_DISCOVERY);
            toID = SE_ID;
        }
        this.perspectiveId = toID;
        IPerspectiveDescriptor fp = registry.findPerspectiveWithId(toID);
        if (fp != null) {
            setImageDescriptor(fp.getImageDescriptor());
        }
    }
}
