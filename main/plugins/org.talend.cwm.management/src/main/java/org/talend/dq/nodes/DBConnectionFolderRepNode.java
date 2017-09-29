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
import org.talend.core.ITDQRepositoryService;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class DBConnectionFolderRepNode extends DQFolderRepNode {

    private ITDQRepositoryService tdqRepService = null;

    /**
     * DOC klliu DBConnectionFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public DBConnectionFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        initDQRepositoryService();
    }

    /**
     * DOC zhao Comment method "updateSoftwareSystem".
     * 
     * @param viewObject
     */
    private void updateSoftwareSystem(IRepositoryViewObject viewObject) {
        Connection connection = ((ConnectionItem) viewObject.getProperty().getItem()).getConnection();
        if (tdqRepService != null && connection instanceof DatabaseConnection) {
            tdqRepService.publishSoftwareSystemUpdateEvent((DatabaseConnection) connection);
        }
    }

    private void initDQRepositoryService() {
        tdqRepService = AnalysisExecutorHelper.getTDQService();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getLabel()
     */
    @Override
    public String getLabel() {
        if (getObject() == null) {
            return this.getProperties(EProperties.LABEL).toString();
        }
        return this.getObject().getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return Messages.getString("DQRepositoryViewLabelProvider.DBConnectionFolderName"); //$NON-NLS-1$
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
        RootContainer<String, IRepositoryViewObject> tdqViewObjects = super.getTdqViewObjects(project, this);
        // sub folders
        // MOD qiongli 2011-1-18.setProperties for every node
        for (Container<String, IRepositoryViewObject> container : tdqViewObjects.getSubContainer()) {
            Folder folder = new Folder((Property) container.getProperty(), ERepositoryObjectType.METADATA_CONNECTIONS);
            if (isIgnoreFolder(withDeleted, project, folder)) {
                continue;
            }
            DBConnectionSubFolderRepNode childNodeFolder = new DBConnectionSubFolderRepNode(folder, this,
                    ENodeType.SIMPLE_FOLDER, project);
            childNodeFolder.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CONNECTIONS);
            childNodeFolder.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CONNECTIONS);
            folder.setRepositoryNode(childNodeFolder);
            super.getChildren().add(childNodeFolder);
        }
        // connection files
        for (IRepositoryViewObject viewObject : tdqViewObjects.getMembers()) {
            if (!withDeleted && viewObject.isDeleted()) {
                continue;
            }
            DBConnectionRepNode repNode = new DBConnectionRepNode(viewObject, this, ENodeType.REPOSITORY_ELEMENT, project);
            repNode.setProperties(EProperties.LABEL, viewObject.getLabel());
            repNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CONNECTIONS);
            viewObject.setRepositoryNode(repNode);
            super.getChildren().add(repNode);

            // Update software system
            updateSoftwareSystem(viewObject);
        }
    }
}
