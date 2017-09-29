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
package org.talend.dataprofiler.core.ui.imex;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.CommonFormEditor;
import org.talend.dataprofiler.core.ui.imex.model.IImportWriter;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;
import org.talend.dataprofiler.core.ui.utils.MessageUI;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ImportWizard extends Wizard {

    private static Logger log = Logger.getLogger(ImportWizard.class);

    protected ImportWizardPage importPage;

    public ImportWizard() {
        setWindowTitle("Import Item");//$NON-NLS-1$ 
        this.importPage = new ImportWizardPage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        addPage(importPage);
    }

    @Override
    public boolean performCancel() {
        final ItemRecord[] records = importPage.getElements();
        if (records.length > 0) {
            final IImportWriter writer = importPage.getWriter();
            try {
                writer.finish(records, null);
                writer.postFinish();
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        return super.performCancel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        // TDQ-10715: only close the DQ editors before import items except the sql editor and text editor.
        // TDQ-13856: when click cancel, do NOT close any editors.
        IWorkbenchPage activePage = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart[] editors = activePage.getEditors();
        for (IEditorPart editor : editors) {
            if (editor instanceof CommonFormEditor) {
                boolean isSaved = activePage.closeEditor(editor, true);
                if (!isSaved) {
                    MessageUI.openWarning(DefaultMessagesImpl.getString("ImportItemAction.closeEditors")); //$NON-NLS-1$
                    return true;
                }
            }
        }
        // TDQ-10715~

        // ADD xqliu TDQ-4284 2011-12-26
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            return true;
        }
        // ~ TDQ-4284

        final ItemRecord[] records = importPage.getElements();

        final IImportWriter writer = importPage.getWriter();

        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("Import Item", records.length);//$NON-NLS-1$ 

                writer.write(records, monitor);

                monitor.done();
            }
        };

        try {
            ProgressUI.popProgressDialog(op);
        } catch (Exception e) {
            log.error(e, e);
        }

        CorePlugin.getDefault().refreshWorkSpace();
        CorePlugin.getDefault().refreshDQView();

        return true;
    }
}
