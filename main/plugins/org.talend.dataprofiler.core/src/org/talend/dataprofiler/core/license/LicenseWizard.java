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

import org.eclipse.jface.wizard.Wizard;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * Wizard for the creation of a new project. <br/>
 * 
 */
public class LicenseWizard extends Wizard {

    /** Main page. */
    private LicenseWizardPage mainPage;

    /**
     * Constructs a new LicenseWizard.
     * 
     * @param author Project author.
     * @param server
     * @param password
     * @param port2
     */
    public LicenseWizard() {
        super();
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        mainPage = new LicenseWizardPage();
        addPage(mainPage);
        setWindowTitle(DefaultMessagesImpl.getString("LicenseWizard.license")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageLib.getImageDescriptor(ImageLib.LICENSE_WIZ));
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        return true;
    }
}
