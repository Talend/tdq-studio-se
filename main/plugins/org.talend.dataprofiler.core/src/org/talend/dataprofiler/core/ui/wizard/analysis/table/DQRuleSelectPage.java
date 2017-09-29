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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.filters.DQFolderFilter;
import org.talend.dataprofiler.core.ui.filters.RuleFolderFliter;
import org.talend.dataprofiler.core.ui.utils.AnalysisUtils;
import org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizardPage;
import org.talend.resource.ResourceManager;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class DQRuleSelectPage extends AbstractAnalysisWizardPage {

    private CheckboxTreeViewer cViewer;

    public CheckboxTreeViewer getCViewer() {
        return cViewer;
    }

    public void setCViewer(CheckboxTreeViewer viewer) {
        cViewer = viewer;
    }

    private final String pageTitle = DefaultMessagesImpl.getString("DQRuleSelectPage.newAnalysis"); //$NON-NLS-1$

    private final String pageMessage = DefaultMessagesImpl.getString("DQRuleSelectPage.selectDQRule"); //$NON-NLS-1$

    public DQRuleSelectPage() {
        setTitle(pageTitle);
        setDescription(pageMessage);
    }

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        FillLayout layout = new FillLayout();
        container.setLayout(layout);

        cViewer = new ContainerCheckedTreeViewer(container, SWT.NONE);
        cViewer.setLabelProvider(new DQRuleLabelProvider());
        cViewer.setContentProvider(new WorkbenchContentProvider());
        cViewer.setInput(ResourceManager.getLibrariesFolder());
        // ADD mzhao bug TDQ-4188 hide the .svn folders.
        cViewer.addFilter(new DQFolderFilter(true));
        cViewer.addFilter(new RuleFolderFliter(true));
        cViewer.addFilter(AnalysisUtils.createRuleFilter());

        setControl(container);
    }

    @Override
    public boolean canFlipToNextPage() {
        return false;
    }

    @Override
    public boolean isCanFinishEarly() {
        return true;
    }

    @Override
    public boolean isHasPages() {
        return false;
    }
}
