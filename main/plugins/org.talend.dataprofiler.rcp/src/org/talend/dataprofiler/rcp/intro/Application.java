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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
// import org.eclipse.ui.internal.tweaklets.Tweaklets;
// import org.eclipse.ui.internal.tweaklets.WorkbenchImplementation;
import org.talend.commons.exception.BusinessException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.utils.TalendPropertiesUtil;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.license.LicenseManagement;
import org.talend.dataprofiler.core.license.LicenseWizard;
import org.talend.dataprofiler.core.license.LicenseWizardDialog;
import org.talend.dataprofiler.rcp.i18n.Messages;
// import org.talend.dataprofiler.rcp.intro.linksbar.Workbench3xImplementation4CoolBar;
import org.talend.registration.wizards.register.TalendForgeDialog;
import org.talend.utils.sugars.ReturnCode;

/**
 * This class controls all aspects of the application's execution.
 */
@SuppressWarnings("restriction")
public class Application implements IApplication {

    protected static Logger log = Logger.getLogger(Application.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
     */
    @Override
    public Object start(IApplicationContext context) {
        Display display = PlatformUI.createDisplay();
        Shell shell = new Shell(display, SWT.ON_TOP);
        // TDQ-12221: do check before use to make sure can popup the "Connect to TalendForge"
        checkBrowserSupport();
        try {
            boolean accept = openLicenseAndRegister(shell);
            if (!accept) {
                return IApplication.EXIT_OK;
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
        }

        try {
            if (!CorePlugin.getDefault().isRepositoryInitialized()) {
                ReturnCode rc = CorePlugin.getDefault().initProxyRepository();
                if (!rc.isOk()) {
                    MessageDialog.openError(shell, DefaultMessagesImpl.getString("Application.warring"), rc.getMessage());//$NON-NLS-1$
                    return IApplication.EXIT_OK;
                }
            }

            // Tweaklets.setDefault(WorkbenchImplementation.KEY, new Workbench3xImplementation4CoolBar());

            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            }
            return IApplication.EXIT_OK;
        } finally {
            display.dispose();
        }
    }

    /**
     * 
     * DOC ggu Comment method "checkForBrowser".
     */
    private void checkBrowserSupport() {
        Shell shell = new Shell();
        try {
            Browser browser = new Browser(shell, SWT.BORDER);
            System.setProperty("USE_BROWSER", Boolean.TRUE.toString()); //$NON-NLS-1$ 
            browser.dispose();
        } catch (Throwable t) {
            System.setProperty("USE_BROWSER", Boolean.FALSE.toString()); //$NON-NLS-1$ 
            log.warn(DefaultMessagesImpl.getString("Application.browser"));
        } finally {
            shell.dispose();

        }
    }

    private boolean openLicenseAndRegister(Shell shell) throws BusinessException {
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        if (!LicenseManagement.isLicenseValidated()) {
            LicenseWizard licenseWizard = new LicenseWizard();
            LicenseWizardDialog dialog = new LicenseWizardDialog(shell, licenseWizard);
            dialog.setTitle(Messages.getString("LicenseWizard.windowTitle")); //$NON-NLS-1$
            if (dialog.open() == WizardDialog.OK) {
                LicenseManagement.acceptLicense();

            } else {
                shell.dispose();
                return false;

            }
        }

        if (brandingService.getBrandingConfiguration().isUseProductRegistration()) {
            IPreferenceStore prefStore = PlatformUI.getPreferenceStore();
            if (brandingService.isPoweredbyTalend()) {
                int count = prefStore.getInt(TalendForgeDialog.LOGINCOUNT);
                if (count < 10 && StringUtils.isEmpty(prefStore.getString("test@talend.com"))) { //$NON-NLS-1$
                    if (TalendPropertiesUtil.isEnabledUseBrowser()) {
                        TalendForgeDialog tfDialog = new TalendForgeDialog(shell, null);
                        tfDialog.setBlockOnOpen(true);
                        tfDialog.open();
                    }
                }
            }
        }
        return true;
    }

    public boolean licenceAccept(Shell shell) {
        if (!LicenseManagement.isLicenseValidated()) {
            LicenseWizard licenseWizard = new LicenseWizard();
            LicenseWizardDialog dialog = new LicenseWizardDialog(shell, licenseWizard);
            dialog.setTitle(Messages.getString("Application.license")); //$NON-NLS-1$
            if (dialog.open() == WizardDialog.OK) {
                LicenseManagement.acceptLicense();
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
    @Override
    public void stop() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) {
            return;
        }
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {

            @Override
            public void run() {
                if (!display.isDisposed()) {
                    workbench.close();
                }
            }
        });
    }

}
