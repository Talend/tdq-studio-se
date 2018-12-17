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
package org.talend.dataprofiler.core.ui.wizard.analysis;

import org.eclipse.jface.wizard.WizardDialog;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.ui.wizard.AbstractWizardPage;
import org.talend.dq.analysis.parameters.AnalysisParameter;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * @author zqin
 */
public abstract class AbstractAnalysisWizardPage extends AbstractWizardPage {

    private boolean canFinishEarly = false, hasPages = true;

    /**
     * @param pageName
     */
    public AbstractAnalysisWizardPage() {
    }

    /**
     * Makes the next page visible.
     */
    public void advanceToNextPageOrFinish() {
        if (canFlipToNextPage()) {
            getContainer().showPage(getNextPage());
        } else if (isCanFinishEarly()) {
            if (getWizard().performFinish()) {
                ((WizardDialog) getContainer()).close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage() {
        if (hasPages) {
            return super.canFlipToNextPage();
        } else {
            return false;
        }
    }

    /**
     * @return the canFinishEarly
     */
    public boolean isCanFinishEarly() {
        return this.canFinishEarly;
    }

    /**
     * @param canFinishEarly the canFinishEarly to set
     */
    public void setCanFinishEarly(boolean canFinishEarly) {
        this.canFinishEarly = canFinishEarly;
    }

    /**
     * @return the hasPages
     */
    public boolean isHasPages() {
        return this.hasPages;
    }

    /**
     * @param hasPages the hasPages to set
     */
    public void setHasPages(boolean hasPages) {
        this.hasPages = hasPages;
    }

    protected AnalysisParameter getConnectionParams() {
        return (AnalysisParameter) super.getParameter();
    }

    protected RepositoryNode getNodeListWithReferenceProject(ERepositoryObjectType type) {
        if (!ProxyRepositoryManager.getInstance().isLocalProject() && !ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            DQRepositoryNode node = new DQRepositoryNode(null, null, ENodeType.SYSTEM_FOLDER, ProjectManager.getInstance()
                    .getCurrentProject());
            node.getChildren().clear();

            java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
            for (Project project : allProjects) {
                RepositoryNode metaRootNode = RepositoryNodeHelper.getRootNode(type, true, project);
                if (metaRootNode != null) {
                    node.getChildren().addAll(metaRootNode.getChildren());
                }
            }
            return node;
        } else {
            return RepositoryNodeHelper.getRootNode(type, true);
        }
    }
}
