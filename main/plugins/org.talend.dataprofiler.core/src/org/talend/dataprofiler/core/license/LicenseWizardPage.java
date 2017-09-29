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
package org.talend.dataprofiler.core.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.service.GlobalServiceRegister;

/**
 * Page for new project details. <br/>
 * 
 */
public class LicenseWizardPage extends WizardPage {

    protected static Logger log = Logger.getLogger(LicenseWizardPage.class);

    /** CLUF field. */
    private Text clufText;

    /**
     * Constructs a new LicenseWizardPage.
     * 
     * @param server
     * @param password
     * @param author
     */
    public LicenseWizardPage() {
        super("WizardPage"); //$NON-NLS-1$

        setTitle(DefaultMessagesImpl.getString("LicenseWizardPage.license")); //$NON-NLS-1$
        // setDescription(Messages.getString("LicenseWizard.description"));
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        ((GridData) parent.getLayoutData()).widthHint = 520;
        ((GridData) parent.getLayoutData()).heightHint = 280;
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);

        Label subTitleLabel = new Label(container, SWT.NONE);
        subTitleLabel.setText(DefaultMessagesImpl.getString("LicenseWizardPage.productLicense")); //$NON-NLS-1$

        clufText = new Text(container, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.LEFT | SWT.BORDER);
        clufText.setBackground(clufText.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        clufText.setEditable(false);
        String license = getLicense();
        clufText.setText(license);

        GridData data = new GridData(GridData.FILL_BOTH);
        clufText.setLayoutData(data);

        setControl(container);
        setPageComplete(true);
    }

    private String getLicense() {
        String license = ""; //$NON-NLS-1$
        BufferedReader in = null;
        try {
            IBrandingService brandingService = GlobalServiceRegister.getDefault().getBrandingService(IBrandingService.class);
            final URL url = brandingService.getLicenseFile();

            FileReader fileReader = new FileReader(new File(url.getPath()));
            in = new BufferedReader(fileReader);

            String licenseLine = ""; //$NON-NLS-1$
            while ((licenseLine = in.readLine()) != null) {
                license += licenseLine + "\n"; //$NON-NLS-1$
            }

        } catch (FileNotFoundException e) {
            log.error(e, e);
        } catch (IOException e) {
            log.error(e, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e, e);
                }
            }
        }
        return license;
    }
}
