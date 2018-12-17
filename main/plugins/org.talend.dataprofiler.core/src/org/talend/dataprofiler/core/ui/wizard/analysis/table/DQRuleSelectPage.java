// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizardPage;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

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
        ResourceViewContentProvider cContentProvider = new ResourceViewContentProvider();
        cViewer.setContentProvider(cContentProvider);

        cViewer.setInput(getNodeListWithReferenceProject(ERepositoryObjectType.TDQ_RULES_SQL));

        // MOD gdbu 2011-7-25 bug : 23220
        ((ResourceViewContentProvider) cContentProvider).setTreeViewer(cViewer);

        // cViewer.setInput(ResourceManager.getLibrariesFolder());
        // // ADD mzhao bug TDQ-4188 hide the .svn folders.
        // cViewer.addFilter(new DQFolderFilter(true));
        // cViewer.addFilter(new RuleFolderFliter(true));
        // cViewer.addFilter(AnalysisUtils.createRuleFilter());

        setControl(container);
    }

    protected RepositoryNode getNodeListWithReferenceProject(ERepositoryObjectType type) {
        if (!ProxyRepositoryManager.getInstance().isLocalProject() && !ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            DQRepositoryNode node = new DQRepositoryNode(null, null, ENodeType.SYSTEM_FOLDER, ProjectManager.getInstance()
                    .getCurrentProject());
            node.getChildren().clear();

            java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
            for (Project project : allProjects) {
                IRepositoryNode metaRootNode = RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.RULES_SQL, project);
                if (metaRootNode != null) {
                    node.getChildren().add(metaRootNode);
                }
            }
            return node;
        } else {
            return RepositoryNodeHelper.getRootNode(type, true);
        }
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
