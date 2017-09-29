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
package org.talend.dataprofiler.core.ui.wizard.indicator;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.ImportFactory;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ImportIndicatorDefinitionWizard extends Wizard {

    private static Logger log = Logger.getLogger(ImportIndicatorDefinitionWizard.class);

    private ImportIndicatorDefinitionWizardPage page;

    @Override
    public boolean performFinish() {
        String targetFile = page.getTargetFile();

        if ("".equals(targetFile)) { //$NON-NLS-1$
            MessageDialog
                    .openError(
                            getShell(),
                            DefaultMessagesImpl.getString("ImportIndicatorDefinitionWizard.Error"), DefaultMessagesImpl.getString("ImportIndicatorDefinitionWizard.SpecifyValidResource")); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        } else {
            try {
                ImportFactory.importFile(new File(targetFile), DefinitionHandler.getTalendDefinitionFile());
            } catch (IOException e) {
                log.error(e, e);
                return false;
            }

        }
        return true;
    }

    @Override
    public void addPages() {
        page = new ImportIndicatorDefinitionWizardPage();
        addPage(page);
    }
}
