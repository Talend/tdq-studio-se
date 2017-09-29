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
package org.talend.dataprofiler.core.ui.wizard.analysis;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.repository.model.RepositoryNode;

/**
 * @author zqin
 */
public class CreateNewAnalysisWizard extends Wizard {

    private IWizard wizard;

    private NewWizardSelectionPage mainPage;

    private WizardPage[] otherPages;

    private FolderProvider currentFolderProvider;

    private RepositoryNode node;

    public FolderProvider getCurrentFolderProvider() {
        return currentFolderProvider;
    }

    public void setCurrentFolderProvider(FolderProvider currentFolderProvider) {
        this.currentFolderProvider = currentFolderProvider;
    }

    public CreateNewAnalysisWizard() {
        setWindowTitle(DefaultMessagesImpl.getString("CreateNewAnalysisWizard.createNewAnalysis")); //$NON-NLS-1$
    }

    /**
     * DOC klliu CreateNewAnalysisWizard constructor comment.
     * 
     * @param path
     * @param node
     */
    public CreateNewAnalysisWizard(RepositoryNode node) {
        setWindowTitle(DefaultMessagesImpl.getString("CreateNewAnalysisWizard.createNewAnalysis")); //$NON-NLS-1$
        this.node = node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        wizard = mainPage.getSelectedWizard();
        wizard.setContainer(getContainer());
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        // mainPage = new NewWizardSelectionPage();
        mainPage = new NewWizardSelectionPage(this.node);
        mainPage.setWizard(wizard);
        addPage(mainPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#canFinish()
     */
    @Override
    public boolean canFinish() {
        if (mainPage != null) {
            return mainPage.isCanFinishEarly();
        }
        return super.canFinish();
    }

    public WizardPage[] getOtherPages() {
        return otherPages;
    }

    public void setOtherPages(WizardPage[] otherPages) {
        this.otherPages = otherPages;
    }
}
