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

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.message.ImportInfoDialog;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
 * 
 */
public class ImportPatternsWizard extends Wizard {

    private IFolder folder;

    private ExpressionType type;

    private ImportPatternsWizardPage page;

    /**
     * DOC qzhang CreateSqlFileWizard constructor comment.
     * 
     * @param folder
     * @param type
     */
    public ImportPatternsWizard(IFolder folder, ExpressionType type) {
        this.folder = folder;
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        page = new ImportPatternsWizardPage();
        addPage(page);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (IMessageProvider.WARNING == page.getMessageType()) {
            if (!MessageDialog.openConfirm(null, DefaultMessagesImpl.getString("ImportPatternsWizard.Warning"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("ImportPatternsWizard.ConfirmImport", page.getMessage()))) { //$NON-NLS-1$
                return false;
            }
        }
        File file = new File(page.getSourceFile());

        final List<ReturnCode> information = ImportFactory.importToStucture(file, folder, type, page.getSkip(), page.getRename());
        if (0 < information.size()) {

            Display.getDefault().asyncExec(new Runnable() {

                public void run() {

                    ImportInfoDialog.openImportInformation(getShell(),
                            DefaultMessagesImpl.getString("ImportInfoDialog.INFO_TSK"), information.toArray(new ReturnCode[0])); //$NON-NLS-1$
                }
            });
        }

        return true;
    }
}
