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
package org.talend.dataprofiler.core.ui.wizard.matchrule;

import org.eclipse.core.resources.IFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.ui.wizard.MetadataWizardPage;
import org.talend.dataprofiler.help.HelpPlugin;
import org.talend.resource.ResourceManager;

/**
 * created by zshen on Aug 19, 2013 Detailled comment
 * 
 */
public class NewMatchRuleMetadataPage extends MetadataWizardPage {

    private String helpContextId;//$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.MetadataWizardPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        pathText.setText(getParameter().getFolderProvider().getFolderURI());

        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                openFolderSelectionDialog(getStoredFolder());
            }
        });
        if (getControl() != null) {
            try {
                PlatformUI.getWorkbench().getHelpSystem()
                        .setHelp(getControl(), HelpPlugin.getDefault().getDQRulesHelpContextID());
            } catch (Exception e) {
                log.error(e, e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.wizard.MetadataWizardPage#createExtendedControl(org.eclipse.swt.widgets.Composite
     * )
     */
    @Override
    protected void createExtendedControl(Composite container) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.MetadataWizardPage#getStoredFolder()
     */
    @Override
    protected IFolder getStoredFolder() {
        return ResourceManager.getRulesMatcherFolder();
    }

    /*
     * Added TDQ-8236 Eclipse help view for the Match rule wizard. TO show the help in the create match rule wizard.
     * 20131022 yyin
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#performHelp()
     */
    @Override
    public void performHelp() {
        PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpContextId);
    }

    public void setHelpContextId(String newHelpContextId) {
        this.helpContextId = newHelpContextId;
    }

}
