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
package org.talend.dataprofiler.core.ui.wizard.analysis.connection;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class ConnectionWizard extends Wizard implements INewWizard {

    private ConnectionTypePage connectionTypePage;

    protected ISelection selection;

    public ConnectionWizard(IWorkbench workbench, Composite relatedComposite) {

        this(workbench, false, false);
        connectionTypePage = new ConnectionTypePage(relatedComposite);
    }

    public ConnectionWizard(IWorkbench workbench, boolean creation, boolean forceReadOnly) {
        super();
        setWindowTitle(DefaultMessagesImpl.getString("ConnectionWizard.createConnection")); //$NON-NLS-1$
    }

    /*
     * According to the first connection type, go to different wizard: database, file, mdm
     */
    @Override
    public void addPages() {
        addPage(connectionTypePage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#canFinish()
     */
    @Override
    public boolean canFinish() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#getContainer()
     */
    @Override
    public IWizardContainer getContainer() {
        return super.getContainer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#getPageCount()
     */
    @Override
    public int getPageCount() {
        return 2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#getPages()
     */
    @Override
    public IWizardPage[] getPages() {
        return super.getPages();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#getStartingPage()
     */
    @Override
    public IWizardPage getStartingPage() {
        return connectionTypePage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#needsPreviousAndNextButtons()
     */
    @Override
    public boolean needsPreviousAndNextButtons() {

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#needsProgressMonitor()
     */
    @Override
    public boolean needsProgressMonitor() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#performCancel()
     */
    @Override
    public boolean performCancel() {

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    @Override
    public boolean performFinish() {

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#setContainer(org.eclipse.jface.wizard.IWizardContainer)
     */
    @Override
    public void setContainer(IWizardContainer wizardContainer) {
        super.setContainer(wizardContainer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;

    }

}
