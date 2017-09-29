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

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC gdbu class global comment. Detailled comment
 */
public class PatternFolderRepNode extends DQFolderRepNode {

    /**
     * DOC msjian PatternFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public PatternFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        return getChildren(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.DQFolderRepNode#getChildrenForProject(boolean, org.talend.core.model.general.Project)
     */
    @Override
    public void getChildrenForProject(boolean withDeleted, Project project) throws PersistenceException {
        // when merge display the ref project items, we will not show the system indicators
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            if (project.isMainProject()) {
                createChildrenNode(withDeleted, project);
            }
        } else {
            createChildrenNode(withDeleted, project);
        }
    }

    /**
     * DOC msjian Comment method "createChildrenNode".
     * 
     * @param withDeleted
     * @param project
     * @throws PersistenceException
     */
    private void createChildrenNode(boolean withDeleted, Project project) throws PersistenceException {
        RootContainer<String, IRepositoryViewObject> tdqViewObjects = super.getTdqViewObjects(project, this);
        // sub folders
        for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
            boolean isRegex = container.getLabel().equals("Regex"); //$NON-NLS-1$

            ERepositoryObjectType eRepositoryObjectType = isRegex ? ERepositoryObjectType.TDQ_PATTERN_REGEX
                    : ERepositoryObjectType.TDQ_PATTERN_SQL;

            Folder folder = new Folder((Property) container.getProperty(), eRepositoryObjectType);
            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }

            DQRepositoryNode patternFolderRepNode;
            if (isRegex) {
                patternFolderRepNode = new PatternRegexFolderRepNode(folder, this, ENodeType.SYSTEM_FOLDER, project);
            } else {
                patternFolderRepNode = new PatternSqlFolderRepNode(folder, this, ENodeType.SYSTEM_FOLDER, project);
            }
            RepositoryNodeHelper.setPropertiesForNode(patternFolderRepNode, eRepositoryObjectType);
            folder.setRepositoryNode(patternFolderRepNode);
            super.getChildren().add(patternFolderRepNode);
        }
    }

}
