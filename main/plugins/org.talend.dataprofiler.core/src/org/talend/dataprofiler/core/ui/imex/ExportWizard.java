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
import org.talend.dataprofiler.core.ui.imex.model.IExportWriter;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ExportWizard extends Wizard {

    private static Logger log = Logger.getLogger(ExportWizard.class);

    protected ExportWizardPage exportPage;

    /**
     * DOC bZhou ExportWizard constructor comment.
     */
    public ExportWizard() {
        this(null);
    }

    /**
     * DOC bZhou ExportWizard constructor comment.
     */
    public ExportWizard(String specifiedPath) {
        setWindowTitle("Export Item");//$NON-NLS-1$ 
        this.exportPage = new ExportWizardPage(specifiedPath);
    }

    public ExportWizard(String specifiedPath, Object[] selectedObj) {
        setWindowTitle("Export Item");//$NON-NLS-1$ 
        this.exportPage = new ExportWizardPage(null, selectedObj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        addPage(exportPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (!exportPage.canFinish()) {
            return false;
        }
        final IExportWriter writer = exportPage.getWriter();
        final ItemRecord[] records = exportPage.getElements();

        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask("Export Item", records.length);//$NON-NLS-1$ 

                writer.write(records, monitor);

                monitor.done();
            }
        };

        try {
            ProgressUI.popProgressDialog(op);
        } catch (Exception e) {
            log.error(e, e);
        }

        return true;
    }
}
