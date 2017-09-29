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

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.ImportFactory;
import org.talend.dataprofiler.core.ui.dialog.message.ImportInfoDialog;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.resource.EResourceConstant;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ImportParserRuleWizard extends Wizard {

    private IFolder folder;

    private ImportParserRuleWizardPage page;

    public ImportParserRuleWizard(IFolder folder) {
        this.folder = folder;
    }

    @Override
    public void addPages() {
        page = new ImportParserRuleWizardPage();
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        if (IMessageProvider.WARNING == page.getMessageType()) {
            if (!MessageDialog.openConfirm(null, DefaultMessagesImpl.getString("ImportParserRuleWizard.Warning"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("ImportParserRuleWizard.ConfirmImport"))) { //$NON-NLS-1$
                return false;
            }
        }
        File file = new File(page.getSourceFile());
        final List<ReturnCode> information = ImportFactory.importParserRuleToStucture(file, folder, page.getSkip(),
                page.getRename());
        if (0 < information.size()) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    ImportInfoDialog.openImportInformation(
                            getShell(),
                            DefaultMessagesImpl.getString("ImportInfoDialog.INFO_TSK"), (ReturnCode[]) information.toArray(new ReturnCode[0])); //$NON-NLS-1$
                }

            });
            CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.RULES_PARSER));
        }
        return true;
    }
}
