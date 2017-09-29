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
package org.talend.dataprofiler.core.ui.wizard.parserrule;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.ui.wizard.MetadataWizardPage;
import org.talend.dataprofiler.help.HelpPlugin;
import org.talend.resource.ResourceManager;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class NewParserRulesWizardPage1 extends MetadataWizardPage {

    protected static Logger log = Logger.getLogger(NewParserRulesWizardPage1.class);

    protected HashMap<String, String> metadata;

    /**
     * DOC xqliu NewDQRulesWizardPage1 constructor comment.
     */
    public NewParserRulesWizardPage1() {
        metadata = new HashMap<String, String>();
        setPageComplete(false);
    }

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

    @Override
    protected void createExtendedControl(Composite container) {
    }

    @Override
    protected IFolder getStoredFolder() {
        return ResourceManager.getRulesFolder();
    }
}
