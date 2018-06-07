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
package org.talend.dq.nodes;

import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
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
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class PatternSqlFolderRepNode extends DQFolderRepNode {

    /**
     * DOC klliu PatternSqlRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public PatternSqlFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
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
        createChildrenNode(withDeleted, project);
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
            Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.TDQ_PATTERN_SQL);
            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }

            PatternSqlSubFolderRepNode childNodeFolder = new PatternSqlSubFolderRepNode(folder, this, ENodeType.SIMPLE_FOLDER,
                    project);
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_PATTERN_SQL);
            childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_PATTERN_SQL);
            super.getChildren().add(childNodeFolder);
        }
        // pattern sql files
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }

            PatternRepNode repNode = new PatternRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.TDQ_PATTERN_SQL);
            repNode.setProperties(EProperties.LABEL, ERepositoryObjectType.TDQ_PATTERN_SQL);
            viewObject.setRepositoryNode(repNode);

            // ADD msjian TDQ-4914: when the node is System sql pattern from ref project, we don't show it(only for
            // merge mode)
            if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
                if (!project.isMainProject()) {
                    ModelElement meNode = RepositoryNodeHelper.getResourceModelElement(repNode);
                    if (meNode != null) {
                        String uuid = RepositoryNodeHelper.getUUID(meNode);
                        if (RepositoryNodeHelper.isSystemSQLPattern(uuid)) {
                            continue;
                        }
                    }
                }
            }
            // TDQ-4914~
            super.getChildren().add(repNode);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.DQFolderRepNode#isIgnoreFolder(boolean, org.talend.core.model.general.Project,
     * org.talend.core.model.repository.Folder)
     */
    @Override
    public boolean isIgnoreFolder(boolean withDeleted, Project project, Folder folder) {
        if (ProxyRepositoryManager.getInstance().isMergeRefProject()) {
            if (!project.isMainProject()) {
                if (RepositoryNodeHelper.isSystemSQLPatternFolder(folder.getLabel())) {
                    return true;
                }
            }
        }
        return super.isIgnoreFolder(withDeleted, project, folder);
    }

    @Override
    public IImage getIcon() {
        return ECoreImage.FOLDER_CLOSE_ICON;
    }

}
