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
package org.talend.dataprofiler.core.ui.utils;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.talend.dataprofiler.core.ui.filters.DQFolderFilter;
import org.talend.dataprofiler.core.ui.filters.FolderObjFilter;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class DQCheckedTreeViewer extends ContainerCheckedTreeViewer {

    private WizardPage wizardPage = null;
    /**
     * DOC bZhou DQCheckedTreeViewer constructor comment.
     * 
     * @param parent
     */
    public DQCheckedTreeViewer(Composite parent) {
        super(parent);
        setLabelProvider(new DQRepositoryViewLabelProvider());
        setContentProvider(new ResourceViewContentProvider());
        addFilter(new DQFolderFilter(true));
        addFilter(new FolderObjFilter());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.ContainerCheckedTreeViewer#doCheckStateChanged(java.lang.Object)
     */
    @Override
    protected void doCheckStateChanged(Object element) {
        super.doCheckStateChanged(element);
        updateWizardStatus();
    }

    /**
     * DOC zshen Comment method "updateWizardStatus".
     * 
     * If nothing be checked then the status of wizard don't should can get into complete status
     */
    public void updateWizardStatus() {
        Object[] checkedElements = this.getCheckedElements();
        wizardPage.setPageComplete(checkedElements.length > 0);
    }

    /**
     * Sets the wizardPage.
     * 
     * @param wizardPage the wizardPage to set
     */
    public void setWizardPage(WizardPage wizardPage) {
        this.wizardPage = wizardPage;
    }

    /**
     * Getter for wizardPage.
     * 
     * @return the wizardPage
     */
    public WizardPage getWizardPage() {
        return wizardPage;
    }

}
