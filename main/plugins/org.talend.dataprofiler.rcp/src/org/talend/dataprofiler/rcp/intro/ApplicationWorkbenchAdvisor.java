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
package org.talend.dataprofiler.rcp.intro;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;
import org.talend.commons.utils.system.EclipseCommandLine;
import org.talend.dataprofiler.rcp.i18n.Messages;
import org.talend.registration.register.RegisterManagement;

/**
 * DOC rli class global comment. Detailled comment <br/>
 * 
 */
@SuppressWarnings("restriction")
public class ApplicationWorkbenchAdvisor extends IDEWorkbenchAdvisor {

    private static Logger log = Logger.getLogger(ApplicationWorkbenchAdvisor.class);

    private static final String PERSPECTIVE_ID = "org.talend.dataprofiler.DataProfilingPerspective"; //$NON-NLS-1$

    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);
        IPreferenceStore apiStore = PlatformUI.getPreferenceStore();
        apiStore.setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
        // PTODO qzhang close the Sql editor, when start up the RCP.
        apiStore.setValue(IWorkbenchPreferenceConstants.CLOSE_EDITORS_ON_EXIT, true);
    }

    @Override
    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor#postStartup()
     */
    @Override
    public void postStartup() {
        super.postStartup();
        if (!ArrayUtils.contains(Platform.getApplicationArgs(), EclipseCommandLine.TALEND_DISABLE_LOGINDIALOG_COMMAND)) {
            RegisterManagement.getInstance().validateRegistration();
        }
        // Start Web Service Registration
        try {
            RegisterManagement.decrementTry();
        } catch (Exception e) {
            log.error(Messages.getString("ApplicationWorkbenchAdvisor.Problem", e.getLocalizedMessage()), e); //$NON-NLS-1$
        }
    }
}
