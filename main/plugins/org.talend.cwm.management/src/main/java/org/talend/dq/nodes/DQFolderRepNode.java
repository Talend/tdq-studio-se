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
package org.talend.dq.nodes;

import java.util.List;

import org.apache.log4j.Logger;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by msjian on 2015年5月7日 Detailled comment
 *
 */
public abstract class DQFolderRepNode extends DQRepositoryNode {

    private static Logger log = Logger.getLogger(DQFolderRepNode.class);

    /**
     * DOC talend DQFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     * @param project
     */
    public DQFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type, Project project) {
        super(object, parent, type, project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren(boolean)
     */
    @Override
    public List<IRepositoryNode> getChildren(boolean withDeleted) {
        try {
            super.getChildren().clear();

            if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
                java.util.Set<Project> allProjects = ProxyRepositoryManager.getInstance().getAllProjects();
                for (Project project : allProjects) {
                    getChildrenForProject(withDeleted, project);
                }
            } else {
                getChildrenForProject(withDeleted, getProject());
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        }

        // MOD gdbu 2011-6-29 bug : 22204
        return filterResultsIfAny(super.getChildren());
        // ~!22204
    }

    /**
     * if the reference project folder node has the same name ignore the case.
     * 
     * @param project
     * @param viewObject
     */
    public boolean isSameNameNodeExist(Project project, Folder folder) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            if (!project.isMainProject()) {
                for (IRepositoryNode node : super.getChildren()) {
                    // we think the node name ignore the case
                    if (node.getLabel().equalsIgnoreCase(folder.getLabel())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * check whether the Folder will be ignored.(means not show in the dq view)
     * 
     * @param withDeleted
     * @param project
     * @param folder
     * @return boolean true: will ignore(not show)
     */
    public boolean isIgnoreFolder(boolean withDeleted, Project project, Folder folder) {
        if (!withDeleted && folder.isDeleted()) {
            return true;
        }

        // we will only show the main node here
        if (isSameNameNodeExist(project, folder)) {
            return true;
        }

        return false;
    }

    /**
     * DOC msjian Comment method "getChildrenForProject".
     * 
     * @param withDeleted
     * @param project
     * @throws PersistenceException
     */
    abstract public void getChildrenForProject(boolean withDeleted, org.talend.core.model.general.Project project)
            throws PersistenceException;

    /**
     * DOC msjian Comment method "getTdqViewObjects".
     * 
     * @param project
     * @param folderNode
     * @return
     * @throws PersistenceException
     */
    public RootContainer<String, IRepositoryViewObject> getTdqViewObjects(org.talend.core.model.general.Project project,
            IRepositoryNode folderNode) throws PersistenceException {
        return ProxyRepositoryFactory.getInstance().getTdqRepositoryViewObjects(project, getContentType(),
                RepositoryNodeHelper.getPath(folderNode).toString());
    }
}
