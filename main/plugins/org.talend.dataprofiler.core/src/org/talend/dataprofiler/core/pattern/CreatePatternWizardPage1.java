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
package org.talend.dataprofiler.core.pattern;

import java.util.HashMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.ui.wizard.MetadataWizardPage;
import org.talend.dataprofiler.help.HelpPlugin;
import org.talend.resource.ResourceManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
 * 
 */

public class CreatePatternWizardPage1 extends MetadataWizardPage {

    protected HashMap<String, String> metadata;

    private String purpose;

    /**
     * DOC qzhang CreateSqlFileWizardPage constructor comment.
     */
    public CreatePatternWizardPage1() {
        metadata = new HashMap<String, String>();
        setPageComplete(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets .Composite)
     */
    @Override
    public void createControl(Composite parent) {

        super.createControl(parent);
        pathText.setText(getParameter().getFolderProvider().getFolderURI());
        if (purpose != null) {
            purposeText.setText(purpose);
        }
        button.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse .swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                openFolderSelectionDialog(getStoredFolder());
            }
        });
        if (getControl() != null) {
            try {
                PlatformUI.getWorkbench().getHelpSystem()
                        .setHelp(getControl(), HelpPlugin.getDefault().getPatternHelpContextID());
            } catch (Exception e) {
                log.error(e, e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.dataprofiler.core.ui.wizard.MetadataWizardPage#
     * createExtendedControl(org.eclipse.swt.widgets.Composite )
     */
    @Override
    protected void createExtendedControl(Composite container) {
        // do nothing at here
    }

    /**
     * Getter for pathText.
     * 
     * @return the pathText
     */
    public Text getPathText() {
        return this.pathText;
    }

    /**
     * Setter for purpose.
     * 
     * @return the purpose
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Override
    protected IFolder getStoredFolder() {
        return ResourceManager.getPatternFolder();
    }

}
